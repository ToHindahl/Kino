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

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (FilmDTO) response.entity();
    }

    public List<FilmDTO> getAllFilme() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_FILME, "", null));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (List<FilmDTO>) response.entity();
    }
}
