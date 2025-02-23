package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.domain.Auffuehrung;
import de.fhdw.Kino.App.repository.AuffuehrungRepository;
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
    private AuffuehrungRepository repository;

    @GetMapping
    public List<Auffuehrung> getAuffuehrungen(
            @RequestParam(name = "datum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {

        List<Auffuehrung> alle = repository.findAll();
        if(datum != null) {
            return alle.stream()
                    .filter(a -> a.getStartzeit().toLocalDate().equals(datum))
                    .collect(Collectors.toList());
        }
        return alle;
    }
}