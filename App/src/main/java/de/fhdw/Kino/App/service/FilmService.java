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
public class FilmService {

    private final CommandProducer commandProducer;

    @Transactional
    public FilmDTO createFilm(FilmDTO dto){

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, "FILM", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (FilmDTO) response.getEntity();
    }

    @Transactional
    public List<FilmDTO> getAllFilme() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse filmeResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "FILMLISTE", null));
        if(filmeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmeResponse.getMessage());
        }

        return (List<FilmDTO>) filmeResponse.getEntity();
    }

    @Transactional
    public void deleteFilm(Long id) {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse filmresponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "FILM", id));
        if (filmresponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && filmresponse.getEntityType().equals("null")) {
            throw new RuntimeException("Film nicht gefunden");
        } else if(filmresponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmresponse.getMessage());
        }

        CommandResponse auffuehrungenResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, "AUFFUEHRUNGSLISTE", null));
        if(auffuehrungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungenResponse.getMessage());
        }

        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) auffuehrungenResponse.getEntity();
        if(auffuehrungen.stream().anyMatch(a -> a.getFilmId().equals(id))) {
            throw new RuntimeException("Film kann nicht gelöscht werden, da er noch Aufführungen hat");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, "FILM", filmresponse.getEntity()));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
