package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.KinoService;
import de.fhdw.Kino.Lib.dto.KinoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kino")
public class KinoController {

    @Autowired
    private KinoService kinoService;

    // Endpunkt zur Initialisierung eines Kinos inkl. Kinosäle, Sitzreihen und Sitzplätzen
    @PostMapping()
    public ResponseEntity<KinoDTO> initializeKino(@Valid @RequestBody KinoDTO dto) {
        return new ResponseEntity<>(kinoService.createKino(dto), HttpStatus.CREATED);
    }

    // Endpunkt zum Abrufen des Kinos
    @GetMapping
    public KinoDTO getKino() {
        return kinoService.getKino();
    }

    // Endpunkt zum Löschen des Kinos
    @DeleteMapping("/reset")
    public ResponseEntity<Void> deleteKino() {
        kinoService.deleteKino();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}