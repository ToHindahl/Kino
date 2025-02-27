package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Film;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.FilmRepository;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    private final AuffuehrungRepository auffuehrungRepository;

    private final KinoRepository kinoRepository;

    @Transactional
    public CommandResponse handleFilmCreation(FilmDTO dto) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Film film = new Film();
        film.setTitel(dto.getTitel());
        filmRepository.save(film);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"success", "film", film.toDTO());
    }

    @Transactional
    public CommandResponse handleFilmRequestAll() {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "filmListe", filmRepository.findAll().stream().map(Film::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleFilmDeletion(Long id) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        if(filmRepository.findById(id).isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Film nicht gefunden", "error");
        }

        if(!auffuehrungRepository.findAll().stream().filter(a -> a.getFilm().getFilmId().equals(id)).toList().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Film kann nicht gelöscht werden, da es noch Aufführungen gibt", "error");
        }

        filmRepository.deleteById(id);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", "");
    }
}
