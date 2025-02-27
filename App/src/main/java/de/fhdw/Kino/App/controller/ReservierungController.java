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

    @PostMapping
    public ResponseEntity<?> createReservierung(@Valid @RequestBody ReservierungDTO req) {
        return new ResponseEntity<>(reservierungService.createReservierung(req), HttpStatus.CREATED);
    }

    // Umwandlung von Reservierung in Buchung
    @PutMapping("/{id}/buchung")
    public ResponseEntity<?> bookReservierung(@PathVariable Long id) {
        return new ResponseEntity<>(reservierungService.bookReservierung(id), HttpStatus.CREATED);
    }

    // Reservierung stornieren
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservierung(@PathVariable Long id) {
        return new ResponseEntity<>(reservierungService.cancelReservierung(id), HttpStatus.CREATED);
    }

    @GetMapping("/auffuehrung/{id}")
    public ResponseEntity<?> getReservierungenByAuffuehrung(@PathVariable Long id) {
        return new ResponseEntity<>(reservierungService.getReservierungenByAuffuehrung(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getReservierungen() {
        return new ResponseEntity<>(reservierungService.getReservierungen(), HttpStatus.OK);
    }
}