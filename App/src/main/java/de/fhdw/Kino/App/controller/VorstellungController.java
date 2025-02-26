package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.AuffuehrungService;
import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vorstellungen")
public class VorstellungController {

    @Autowired
    private AuffuehrungService auffuehrungService;

    @GetMapping
    public Double getVorstellungen(
            @RequestParam(name = "datum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {

        //TODO

        List<AuffuehrungDTO> auffuehrungDTOs = auffuehrungService.getAllAuffuehrungen();

        if(datum != null) {
            return new Double(null, auffuehrungDTOs.stream()
                    .filter(a -> a.startzeit().toLocalDate().equals(datum))
                    .collect(Collectors.toList()));
        }
        return new Double(null, auffuehrungDTOs);
    }


}