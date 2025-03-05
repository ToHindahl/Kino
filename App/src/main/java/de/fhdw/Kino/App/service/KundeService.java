package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KundeService {

    private final CommandProducer commandProducer;

    @Transactional
    public KundeDTO createKunde(KundeDTO dto){

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, CommandRequest.RequestEntityType.KUNDE, dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (KundeDTO) response.getEntity();
    }

    @Transactional
    public List<KundeDTO> getAllKunden() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KUNDE, new KundeDTO()));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<KundeDTO>) response.getEntity();
    }

    @Transactional
    public void deleteKunde(Long id) {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse kundeResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KUNDE, new KundeDTO(id, null, null, null, null)));
        if (kundeResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kundeResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kunde nicht gefunden");
        } else if(kundeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kundeResponse.getMessage());
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO()));
        if (reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        if(reservierungen.stream().anyMatch(r -> r.getKundeId().equals(id))) {
            throw new RuntimeException("Kunde hat noch Reservierungen");
        }

        CommandResponse response =  commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, CommandRequest.RequestEntityType.KUNDE, kundeResponse.getEntity()));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
