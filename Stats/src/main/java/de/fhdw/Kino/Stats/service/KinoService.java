package de.fhdw.Kino.Stats.service;

import de.fhdw.Kino.Lib.dto.*;
import de.fhdw.Kino.Stats.model.KinoDocument;
import de.fhdw.Kino.Stats.repository.KinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KinoService {

    private final KinoRepository kinoRepository;

    public void createKino(KinoDTO kinoDTO) {
        KinoDocument kinoDocument = new KinoDocument();
        kinoDocument.setKinoId(kinoDTO.getKinoId());
        kinoDocument.setKinoName(kinoDTO.getName());

        // Sitzplätze mappen
        List<KinoDocument.Sitzplatz> sitzplaetze = kinoDTO.getKinosaele().stream()
                .flatMap(saal -> saal.getSitzreihen().stream()
                        .flatMap(reihe -> reihe.getSitzplaetze().stream()
                                .map(platz -> new KinoDocument.Sitzplatz(platz.getSitzplatzId(), reihe.getSitzreihenTyp().name()))
                        )
                ).toList();
        kinoDocument.setSitzplaetze(sitzplaetze);

        // Initial leere Filme-Liste
        kinoDocument.setFilme(List.of());

        kinoRepository.save(kinoDocument);
    }

    public void addFilm(FilmDTO filmDTO) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));
        KinoDocument.Film film = new KinoDocument.Film();
        film.setFilmId(filmDTO.getFilmId());
        film.setFilmtitel(filmDTO.getTitel());
        film.setAuffuehrungen(List.of()); // Initial leere Aufführungen-Liste

        kinoDocument.getFilme().add(film);
        kinoRepository.save(kinoDocument);
    }

    public void addAuffuehrung(AuffuehrungDTO auffuehrungDTO) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));
        KinoDocument.Auffuehrung auffuehrung = new KinoDocument.Auffuehrung();
        auffuehrung.setAuffuehrungId(auffuehrungDTO.getAuffuehrungId());
        auffuehrung.setReservierungen(List.of()); // Initial leere Reservierungen-Liste

        // Finde den Film und füge die Aufführung hinzu
        kinoDocument.getFilme().stream()
                .filter(f -> f.getFilmId().equals(auffuehrungDTO.getFilmId()))
                .findFirst()
                .ifPresent(film -> film.getAuffuehrungen().add(auffuehrung));

        kinoRepository.save(kinoDocument);
    }

    public void addReservierung(ReservierungDTO reservierungDTO) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));
        KinoDocument.Reservierung reservierung = new KinoDocument.Reservierung();
        reservierung.setSitzplatzIds(reservierungDTO.getSitzplatzIds());

        // Finde die Aufführung und füge die Reservierung hinzu
        kinoDocument.getFilme().stream()
                .flatMap(film -> film.getAuffuehrungen().stream())
                .filter(auffuehrung -> auffuehrung.getAuffuehrungId().equals(reservierungDTO.getAuffuehrungId()))
                .findFirst()
                .ifPresent(auffuehrung -> auffuehrung.getReservierungen().add(reservierung));

        kinoRepository.save(kinoDocument);
    }

    public void deleteAuffuehrung(Long id) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));
        kinoDocument.getFilme().forEach(film -> film.getAuffuehrungen().removeIf(auffuehrung -> auffuehrung.getAuffuehrungId().equals(id)));

        kinoRepository.save(kinoDocument);
    }

    public void deleteFilm(Long id) {
        KinoDocument kinoDocument = kinoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kino nicht gefunden"));
        kinoDocument.getFilme().removeIf(film -> film.getFilmId().equals(id));

        kinoRepository.save(kinoDocument);
    }

    public void reset() {
        kinoRepository.deleteAll();
    }
}