package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.domain.Auffuehrung;
import de.fhdw.Kino.App.domain.Film;
import de.fhdw.Kino.App.dto.AuffuehrungDto;
import de.fhdw.Kino.App.repository.AuffuehrungRepository;
import de.fhdw.Kino.App.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class AuffuehrungService {

    @Autowired
    private AuffuehrungRepository auffuehrungRepository;

    @Autowired
    private FilmRepository filmRepository;

    public Auffuehrung createAuffuehrung(AuffuehrungDto auffuehrungDto){
        Optional<Film> filmOpt = filmRepository.findById(auffuehrungDto.getFilm().getId());
        if(filmOpt.isEmpty()){
            throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Ungültige Film-ID.", null, null, null);
        }
        Auffuehrung auffuehrung = new Auffuehrung();
        auffuehrung.setId(auffuehrungDto.getId());
        auffuehrung.setFilm(auffuehrungDto.getFilm());
        auffuehrung.setSaal(auffuehrungDto.getSaal());
        auffuehrung.setStartzeit(auffuehrungDto.getStartzeit());
        return auffuehrungRepository.save(auffuehrung);
    }
}
