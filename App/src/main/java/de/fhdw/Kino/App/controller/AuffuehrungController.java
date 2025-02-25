package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.service.AuffuehrungService;
import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auffuehrungen")
public class AuffuehrungController {

    @Autowired
    private AuffuehrungService auffuehrungService;

    @GetMapping
    public List<AuffuehrungDTO> getAuffuehrungen(
            @RequestParam(name = "datum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {

        List<AuffuehrungDTO> auffuehrungDTOs = auffuehrungService.getAllAuffuehrungen();

        if(datum != null) {
            return auffuehrungDTOs.stream()
                    .filter(a -> a.auffuehrungStartzeit().toLocalDate().equals(datum))
                    .collect(Collectors.toList());
        }
        return auffuehrungDTOs;
    }
}