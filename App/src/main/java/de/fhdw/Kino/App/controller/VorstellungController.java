package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.AuffuehrungService;
import de.fhdw.Kino.App.service.FilmService;
import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vorstellungen")
public class VorstellungController {

    @Autowired
    private AuffuehrungService auffuehrungService;
    @Autowired
    private FilmService filmService;

    // Endpunkt zum Abrufen aller Filme
    @GetMapping("/filme")
    public List<FilmDTO> getFilme() {
        return filmService.getAllFilme();
    }

    // Endpunkt zum Anlegen eines Films
    @PostMapping("/film")
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO film) {
        return new ResponseEntity<>(filmService.createFilm(film), HttpStatus.CREATED);
    }

    // Endpunkt zum Löschen eines Films
    @DeleteMapping("/film/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpunkt zum Abrufen aller Aufführungen
    @GetMapping("/auffuehrungen")
    public List<AuffuehrungDTO> getAuffuehrung() {
        List<AuffuehrungDTO> auffuehrungDTOs = auffuehrungService.getAllAuffuehrungen();
        return auffuehrungDTOs;
    }

    // Endpunkt zum Anlegen einer Aufführung
    @PostMapping("/auffuehrung")
    public ResponseEntity<AuffuehrungDTO> createAuffuehrung(@Valid @RequestBody AuffuehrungDTO auffuehrung) {
        return new ResponseEntity<>(auffuehrungService.createAuffuehrung(auffuehrung), HttpStatus.CREATED);
    }

    // Endpunkt zum Löschen einer Aufführung
    @DeleteMapping("/auffuehrung/{id}")
    public ResponseEntity<Void> deleteAuffuehrung(@PathVariable Long id) {
        auffuehrungService.deleteAuffuehrung(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}