package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.AuffuehrungService;
import de.fhdw.Kino.App.service.FilmService;
import de.fhdw.Kino.App.service.KinoService;
import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import de.fhdw.Kino.Lib.dto.KinoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/init")
public class InitializationController {

    @Autowired
    private KinoService kinoService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private AuffuehrungService auffuehrungService;

    // Endpunkt zur Initialisierung eines Kinos inkl. Säle, Reihen und Sitzplätzen
    @PostMapping("/kino")
    public ResponseEntity<KinoDTO> initializeKino(@RequestBody KinoDTO kino) {
        return new ResponseEntity<>(kinoService.createKino(kino), HttpStatus.CREATED);
    }

    // Endpunkt zum Anlegen eines Films
    @PostMapping("/film")
    public ResponseEntity<FilmDTO> createFilm(@RequestBody FilmDTO film) {
        return new ResponseEntity<>(filmService.createFilm(film), HttpStatus.CREATED);
    }

    // Endpunkt zum Anlegen einer Aufführung
// Erwartet wird ein JSON, in dem u.a. Startzeit, ein gültiger Film (nur ID erforderlich)
// und ein Kinosaal (ebenfalls referenziert über ID) übergeben werden.
    @PostMapping("/auffuehrung")
    public ResponseEntity<AuffuehrungDTO> createAuffuehrung(@RequestBody AuffuehrungDTO auffuehrung) {
        return new ResponseEntity<>(auffuehrungService.createAuffuehrung(auffuehrung), HttpStatus.CREATED);
    }
}