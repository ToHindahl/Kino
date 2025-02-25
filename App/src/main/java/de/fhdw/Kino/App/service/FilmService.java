package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.FilmProducer;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmProducer filmProducer;

    public FilmDTO createFilm(FilmDTO filmDto){

        CreationResponseDTO response = filmProducer.createFilm(filmDto);

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return new FilmDTO(response.id(), filmDto.filmTitel());
    }

    public List<FilmDTO> getAllFilme() {
        return filmProducer.getAllFilme().filme();
    }

}
