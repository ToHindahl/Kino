package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KinoService {

    private final CommandProducer commandProducer;

    @Transactional
    public KinoDTO createKino(KinoDTO dto){

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && !kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino bereits initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, CommandRequest.RequestEntityType.KINO, dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (KinoDTO) response.getEntity();
    }

    @Transactional
    public KinoDTO getKino() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && response.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());

        }

        return (KinoDTO) response.getEntity();
    }

    @Transactional
    public void deleteKino() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());

        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, CommandRequest.RequestEntityType.KINO, kinoResponse.getEntity()));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
