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

    @GetMapping("/auffuehrungen")
    public List<AuffuehrungDTO> getAuffuehrung(
            @RequestParam(name = "datum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {
        List<AuffuehrungDTO> auffuehrungDTOs = auffuehrungService.getAllAuffuehrungen();

        if(datum != null) {
            return auffuehrungDTOs.stream().filter(a -> a.getStartzeit().toLocalDate().equals(datum)).toList();
        }

        return auffuehrungDTOs;
    }

    @GetMapping("/filme")
    public List<FilmDTO> getFilme() {
        return filmService.getAllFilme();
    }

    // Endpunkt zum Anlegen eines Films
    @PostMapping("/film")
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO film) {
        return new ResponseEntity<>(filmService.createFilm(film), HttpStatus.CREATED);
    }

    // Endpunkt zum Anlegen einer Aufführung
// Erwartet wird ein JSON, in dem u.a. Startzeit, ein gültiger Film (nur ID erforderlich)
// und ein Kinosaal (ebenfalls referenziert über ID) übergeben werden.
    @PostMapping("/auffuehrung")
    public ResponseEntity<AuffuehrungDTO> createAuffuehrung(@Valid @RequestBody AuffuehrungDTO auffuehrung) {
        return new ResponseEntity<>(auffuehrungService.createAuffuehrung(auffuehrung), HttpStatus.CREATED);
    }


}