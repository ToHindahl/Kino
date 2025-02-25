package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.domain.Auffuehrung;
import de.fhdw.Kino.App.domain.Kunde;
import de.fhdw.Kino.App.domain.Reservierung;
import de.fhdw.Kino.App.domain.ReservierungsStatus;
import de.fhdw.Kino.App.dto.ReservierungRequest;
import de.fhdw.Kino.App.repository.AuffuehrungRepository;
import de.fhdw.Kino.App.repository.KundeRepository;
import de.fhdw.Kino.App.repository.ReservierungRepository;
import de.fhdw.Kino.App.service.ReservierungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reservierungen")
public class ReservierungController {

    @Autowired
    private ReservierungRepository reservierungRepository;

    @Autowired
    private AuffuehrungRepository auffuehrungRepository;

    @Autowired
    private KundeRepository kundeRepository;

    @Autowired
    private ReservierungService reservierungService;

    @PostMapping
    public ResponseEntity<?> createReservierung(@Valid @RequestBody ReservierungRequest req) {
        Optional<Auffuehrung> auffOpt = auffuehrungRepository.findById(req.getAuffuehrungId());
        Optional<Kunde> kundeOpt = kundeRepository.findById(req.getKundeId());
        if(auffOpt.isEmpty() || kundeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Ungültige Aufführungs- oder Kunden-ID.");
        }
        Reservierung reservierung = new Reservierung();
        reservierung.setAuffuehrung(auffOpt.get());
        reservierung.setKunde(kundeOpt.get());
        reservierung.setSitzplatzIds(req.getSitzplatzIds());
        reservierung.setStatus(ReservierungsStatus.RESERVED);
        reservierungService.createReservierung(req);
        return new ResponseEntity<>(reservierungRepository.save(reservierung), HttpStatus.CREATED);
    }

    // Umwandlung von Reservierung in Buchung
    @PutMapping("/{id}/buchung")
    public ResponseEntity<?> convertToBuchung(@PathVariable Long id) {
        Optional<Reservierung> opt = reservierungRepository.findById(id);
        if(opt.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Reservierung res = opt.get();
        if(res.getStatus() != ReservierungsStatus.RESERVED) {
            return ResponseEntity.badRequest().body("Reservierung ist nicht im Zustand 'RESERVED'.");
        }
        res.setStatus(ReservierungsStatus.BOOKED);
        reservierungRepository.save(res);
        return ResponseEntity.ok(res);
    }

    // Reservierung stornieren
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservierung(@PathVariable Long id) {
        Optional<Reservierung> opt = reservierungRepository.findById(id);
        if(opt.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Reservierung res = opt.get();
        if(res.getStatus() == ReservierungsStatus.CANCELLED) {
            return ResponseEntity.badRequest().body("Reservierung wurde bereits storniert.");
        }
        res.setStatus(ReservierungsStatus.CANCELLED);
        reservierungRepository.save(res);
        return ResponseEntity.ok(res);
    }
}