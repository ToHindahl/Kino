package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Film;
import de.fhdw.Kino.DB.repository.FilmRepository;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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
    public CommandResponse handleFilmRequest(Long id) {
        Optional<Film> film = filmRepository.findById(id);
        return film.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "film", value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", "null"));
    }

    @Transactional
    public CommandResponse handleFilmRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "filmListe", filmRepository.findAll().stream().map(Film::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleFilmDeletion(FilmDTO dto) {
        filmRepository.deleteById(dto.getFilmId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", "null");
    }
}
