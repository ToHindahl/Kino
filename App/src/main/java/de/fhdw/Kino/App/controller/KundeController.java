package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.producers.KundeProducer;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kunden")
public class KundeController {

    @Autowired
    private KundeProducer kundeProducer;

    @PostMapping
    public ResponseEntity<KundeDTO> createKunde(@RequestBody KundeDTO kunde) {
        return new ResponseEntity<>(new KundeDTO(kundeProducer.createKunde(kunde).id(), kunde.kundeVorname(), kunde.kundeNachname(), kunde.kundeEmail()), HttpStatus.CREATED);
    }
}