package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Film;
import de.fhdw.Kino.DB.repositories.FilmRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    @Transactional
    public CommandResponse handleFilmCreation(FilmDTO dto) {
        Film film = new Film();
        film.setTitel(dto.getTitel());
        filmRepository.save(film);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"success", "film", film.toDTO());
    }

    @Transactional
    public CommandResponse handleFilmRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "filmListe", filmRepository.findAll().stream().map(Film::toDTO).toList());
    }

}
