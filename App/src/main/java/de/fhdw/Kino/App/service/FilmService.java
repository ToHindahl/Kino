package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final CommandProducer commandProducer;

    public FilmDTO createFilm(FilmDTO dto){

        UUID transactionId = UUID.randomUUID();

        Optional<KinoDTO> kino = (Optional<KinoDTO>) commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null)).getEntity();

        if(kino.isEmpty()) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(null, CommandRequest.Operation.CREATE, "FILM", dto));


        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (FilmDTO) response.getEntity();
    }

    public List<FilmDTO> getAllFilme() {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));

        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType() == "null") {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse filmeResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "FILM", null));

        if(filmeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmeResponse.getMessage());
        }

        return (List<FilmDTO>) filmeResponse.getEntity();
    }

    public void deleteFilm(Long id) {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));

        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType() == "null") {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse filmresponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "FILM", id));

        if (filmresponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && filmresponse.getEntityType() == "null") {
            throw new RuntimeException("Film nicht gefunden");
        } else if(filmresponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmresponse.getMessage());
        }

        CommandResponse auffuehrungenResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "AUFFUEHRUNG", null));

        if(auffuehrungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungenResponse.getMessage());
        }

        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) auffuehrungenResponse.getEntity();

        if(auffuehrungen.stream().anyMatch(a -> a.getFilmId().equals(id))) {
            throw new RuntimeException("Film kann nicht gelöscht werden, da er noch Aufführungen hat");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "FILM", filmresponse.getEntity()));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
