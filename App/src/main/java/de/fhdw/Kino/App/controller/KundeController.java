package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.domain.Kunde;
import de.fhdw.Kino.App.repository.KundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kunden")
public class KundeController {

    @Autowired
    private KundeRepository repository;

    @PostMapping
    public ResponseEntity<Kunde> createKunde(@RequestBody Kunde kunde) {
        return new ResponseEntity<>(repository.save(kunde), HttpStatus.CREATED);
    }
}