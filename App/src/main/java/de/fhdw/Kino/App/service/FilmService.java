package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final CommandProducer commandProducer;

    public FilmDTO createFilm(FilmDTO dto){

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CREATE_FILM, "film", dto));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (FilmDTO) response.getEntity();
    }

    public List<FilmDTO> getAllFilme() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_FILME, ""));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<FilmDTO>) response.getEntity();
    }

    public void deleteFilm(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.DELETE_FILM, "id", id));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
