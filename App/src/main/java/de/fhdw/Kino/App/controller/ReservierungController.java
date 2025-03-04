package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.ReservierungService;
import de.fhdw.Kino.Lib.dto.ReservierungDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservierungen")
public class ReservierungController {

    @Autowired
    private ReservierungService reservierungService;

    // Endpunkt zum Anlegen einer Reservierung
    @PostMapping
    public ResponseEntity<?> createReservierung(@Valid @RequestBody ReservierungDTO req) {
        return new ResponseEntity<>(reservierungService.createReservierung(req), HttpStatus.CREATED);
    }

    // Endpunkt zum Buchen einer Reservierung
    @PutMapping("/{id}/buchen")
    public ResponseEntity<?> bookReservierung(@PathVariable Long id) {
        return new ResponseEntity<>(reservierungService.bookReservierung(id), HttpStatus.CREATED);
    }

    // Endpunkt zum Stornieren einer Reservierung
    @PutMapping("/{id}/stornieren")
    public ResponseEntity<?> cancelReservierung(@PathVariable Long id) {
        return new ResponseEntity<>(reservierungService.cancelReservierung(id), HttpStatus.CREATED);
    }

    // Endpunkt zum Abrufen aller Reservierungen
    @GetMapping
    public ResponseEntity<?> getReservierungen() {
        return new ResponseEntity<>(reservierungService.getReservierungen(), HttpStatus.OK);
    }
}