package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.KundeService;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class KundeController {

    @Autowired
    private KundeService kundeService;

    // Endpunkt zum Anlegen eines Kunden
    @PostMapping("/kunde")
    public ResponseEntity<KundeDTO> createKunde(@Valid @RequestBody KundeDTO dto) {
        return new ResponseEntity<>(kundeService.createKunde(dto), HttpStatus.CREATED);
    }

    // Endpunkt zum Löschen eines Kunden
    @DeleteMapping("/kunde/{id}")
    public ResponseEntity<Void> deleteKunde(@PathVariable Long id) {
        kundeService.deleteKunde(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpunkt zum Abrufen aller Kunden
    @GetMapping("/kunden")
    public ResponseEntity<?> getKunden() {
        return new ResponseEntity<>(kundeService.getAllKunden(), HttpStatus.OK);
    }
}