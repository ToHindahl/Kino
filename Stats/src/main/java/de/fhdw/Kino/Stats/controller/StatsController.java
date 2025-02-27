package de.fhdw.Kino.Stats.controller;

import de.fhdw.Kino.Stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/auffuehrung/{auffuehrungId}")
    public double getEinnahmenByAuffuehrung(@PathVariable Long auffuehrungId) {
        return statsService.getEinnahmenByAuffuehrung(auffuehrungId);
    }

    @GetMapping("/film/{filmId}")
    public double getEinnahmenByFilm(@PathVariable Long filmId) {
        return statsService.getEinnahmenByFilm(filmId);
    }
}