package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.domain.*;
import de.fhdw.Kino.App.repository.AuffuehrungRepository;
import de.fhdw.Kino.App.repository.FilmRepository;
import de.fhdw.Kino.App.repository.KinoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/init")
public class InitializationController {

    @Autowired
    private KinoRepository kinoRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private AuffuehrungRepository auffuehrungRepository;

    // Endpunkt zur Initialisierung eines Kinos inkl. Säle, Reihen und Sitzplätzen
    @PostMapping("/kino")
    public ResponseEntity<Kino> initializeKino(@RequestBody Kino kino) {
        // Setze Rückreferenzen für Säle, Reihen und Sitzplätze
        setParentReferences(kino);
        Kino savedKino = kinoRepository.save(kino);
        return new ResponseEntity<>(savedKino, HttpStatus.CREATED);
    }

    // Hilfsmethode zur Initialisierung der Rückreferenzen
    private void setParentReferences(Kino kino) {
        if (kino.getSaele() != null) {
            kino.getSaele().forEach(saal -> {
                saal.setKino(kino); // Setze die Kino-Referenz in jedem Saal

                if (saal.getReihen() != null) {
                    saal.getReihen().forEach(reihe -> {
                        reihe.setSaal(saal); // Setze die Kinosaal-Referenz in jeder Reihe

                        if (reihe.getSitze() != null) {
                            reihe.getSitze().forEach(sitz -> {
                                sitz.setReihe(reihe); // Setze die Reihe-Referenz in jedem Sitzplatz
                            });
                        }
                    });
                }
            });
        }
    }

    // Endpunkt zum Anlegen eines Films
    @PostMapping("/film")
    public ResponseEntity<Film> initializeFilm(@RequestBody Film film) {
        Film savedFilm = filmRepository.save(film);
        return new ResponseEntity<>(savedFilm, HttpStatus.CREATED);
    }

    // Endpunkt zum Anlegen einer Aufführung
// Erwartet wird ein JSON, in dem u.a. Startzeit, ein gültiger Film (nur ID erforderlich)
// und ein Kinosaal (ebenfalls referenziert über ID) übergeben werden.
    @PostMapping("/auffuehrung")
    public ResponseEntity<?> initializeAuffuehrung(@RequestBody Auffuehrung auffuehrung) {

        Optional<Film> optFilm = filmRepository.findById(auffuehrung.getFilm().getId());

        if(optFilm.isEmpty()){
            return ResponseEntity.badRequest().body("Film-ID nicht gefunden.");
        }

        if(auffuehrung.getStartzeit() == null){
            return ResponseEntity.badRequest().body("Keine Startzeit angegeben.");
        }

        if(kinoRepository.findAll().get(0).getSaele().contains(auffuehrung.getSaal())){
            return ResponseEntity.badRequest().body("Saal nicht gefunden.");
        }



        Auffuehrung savedAuffuehrung = auffuehrungRepository.save(auffuehrung);
        return new ResponseEntity<>(savedAuffuehrung, HttpStatus.CREATED);
    }
}