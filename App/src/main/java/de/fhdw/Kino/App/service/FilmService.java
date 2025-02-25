package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.domain.Film;
import de.fhdw.Kino.App.dto.FilmDto;
import de.fhdw.Kino.App.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public Film createFilm(FilmDto filmDto){
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setTitel(filmDto.getTitel());
        return filmRepository.save(film);
    }
}
