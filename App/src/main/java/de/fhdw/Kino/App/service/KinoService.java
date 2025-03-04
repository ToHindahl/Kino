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
public class KinoService {

    private final CommandProducer commandProducer;

    @Transactional
    public KinoDTO createKino(KinoDTO dto){

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && !kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino bereits initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, "KINO", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (KinoDTO) response.getEntity();
    }

    @Transactional
    public KinoDTO getKino() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && response.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());

        }

        return (KinoDTO) response.getEntity();
    }

    @Transactional
    public void deleteKino() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());

        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "RESERVIERUNGSLISTE", null)).getEntity();
        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "AUFFUEHRUNGSLISTE", null)).getEntity();
        List<FilmDTO> filme = (List<FilmDTO>) commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "FILMLISTE", null)).getEntity();
        List<KundeDTO> kunden = (List<KundeDTO>) commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KUNDENLISTE", null)).getEntity();

        reservierungen.forEach(reservierung -> {commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "RESERVIERUNG", reservierung));});
        auffuehrungen.forEach(auffuehrung -> {commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "AUFFUEHRUNG", auffuehrung));});
        filme.forEach(film -> {commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "FILM", film));});
        kunden.forEach(kunde -> {commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "KUNDE", kunde));});

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "KINO", kinoResponse.getEntity()));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
