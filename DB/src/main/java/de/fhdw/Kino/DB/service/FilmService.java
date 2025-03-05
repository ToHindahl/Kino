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
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"success", CommandResponse.ResponseEntityType.FILM, film.toDTO());
    }

    @Transactional
    public CommandResponse handleFilmRequest(Long id) {
        Optional<Film> film = filmRepository.findById(id);
        return film.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", CommandResponse.ResponseEntityType.FILM, value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", CommandResponse.ResponseEntityType.EMPTY));
    }

    @Transactional
    public CommandResponse handleFilmRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", CommandResponse.ResponseEntityType.FILMLISTE, filmRepository.findAll().stream().map(Film::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleFilmDeletion(FilmDTO dto) {

        Optional<Film> film = filmRepository.findById(dto.getFilmId());
        if(film.isEmpty()){
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", CommandResponse.ResponseEntityType.EMPTY);
        }

        if(!dto.getVersion().equals(film.get().getVersion())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "version mismatch", CommandResponse.ResponseEntityType.EMPTY);
        }

        filmRepository.deleteById(dto.getFilmId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", CommandResponse.ResponseEntityType.EMPTY);
    }
}
