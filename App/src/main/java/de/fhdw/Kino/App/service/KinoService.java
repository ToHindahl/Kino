package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KinoService {

    private final CommandProducer commandProducer;

    public KinoDTO createKino(KinoDTO dto){

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(null, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && !kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino bereits initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.CREATE, "KINO", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (KinoDTO) response.getEntity();
    }

    public KinoDTO getKino() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(null, CommandRequest.Operation.READ, "KINO", null));
        if (response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && response.getEntityType() == "null") {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());

        }

        return (KinoDTO) response.getEntity();
    }

    public void deleteKino() {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(null, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType() == "null") {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());

        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "RESERVIERUNG", null)).getEntity();
        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "AUFFUEHRUNG", null)).getEntity();
        List<FilmDTO> filme = (List<FilmDTO>) commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "FILM", null)).getEntity();
        List<KundeDTO> kunden = (List<KundeDTO>) commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "KUNDE", null)).getEntity();

        reservierungen.forEach(reservierung -> {commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "RESERVIERUNG", reservierung));});
        auffuehrungen.forEach(auffuehrung -> {commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "AUFFUEHRUNG", auffuehrung));});
        filme.forEach(film -> {commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "FILM", film));});
        kunden.forEach(kunde -> {commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "KUNDE", kunde));});

        commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "KINO", kinoResponse.getEntity()));

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.COMMIT, "COMMIT", null));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
