package de.fhdw.Kino.Stats.service;

import de.fhdw.Kino.Lib.dto.SitzreiheDTO;
import de.fhdw.Kino.Stats.model.KinoDocument;
import de.fhdw.Kino.Stats.repository.KinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final KinoRepository kinoRepository;

    public double getEinnahmenByAuffuehrung(Long auffuehrungId) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));

        return kinoDocument.getFilme().stream()
                .flatMap(film -> film.getAuffuehrungen().stream())
                .filter(auffuehrung -> auffuehrung.getAuffuehrungId().equals(auffuehrungId))
                .flatMap(auffuehrung -> auffuehrung.getReservierungen().stream())
                .mapToDouble(reservierung -> reservierung.getSitzplatzIds().stream()
                        .mapToDouble(sitzplatzId -> {
                            // Finde die Kategorie des Sitzplatzes
                            String kategorie = kinoDocument.getSitzplaetze().stream()
                                    .filter(sitzplatz -> sitzplatz.getSitzplatzId().equals(sitzplatzId))
                                    .findFirst()
                                    .map(KinoDocument.Sitzplatz::getKategorie)
                                    .orElseThrow(() -> new RuntimeException("Sitzplatz nicht gefunden"));

                            // Ermittle den Preis basierend auf der Kategorie
                            return SitzreiheDTO.SitzreihenTypDTO.valueOf(kategorie).price;
                        })
                        .sum())
                .sum();
    }

    public double getEinnahmenByFilm(Long filmId) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));

        return kinoDocument.getFilme().stream()
                .filter(film -> film.getFilmId().equals(filmId))
                .flatMap(film -> film.getAuffuehrungen().stream())
                .flatMap(auffuehrung -> auffuehrung.getReservierungen().stream())
                .mapToDouble(reservierung -> reservierung.getSitzplatzIds().stream()
                        .mapToDouble(sitzplatzId -> {
                            // Finde die Kategorie des Sitzplatzes
                            String kategorie = kinoDocument.getSitzplaetze().stream()
                                    .filter(sitzplatz -> sitzplatz.getSitzplatzId().equals(sitzplatzId))
                                    .findFirst()
                                    .map(KinoDocument.Sitzplatz::getKategorie)
                                    .orElseThrow(() -> new RuntimeException("Sitzplatz nicht gefunden"));

                            // Ermittle den Preis basierend auf der Kategorie
                            return SitzreiheDTO.SitzreihenTypDTO.valueOf(kategorie).price;
                        })
                        .sum())
                .sum();
    }
}