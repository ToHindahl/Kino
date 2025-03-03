package de.fhdw.Kino.Stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

@Data
@Document
public class KinoDocument {

    @Id
    private Long kinoId;

    private List<Sitzplatz> sitzplaetze;
    private List<Film> filme;

    @Data
    @AllArgsConstructor
    public static class Sitzplatz {
        @Id
        private Long sitzplatzId;

        private String kategorie;
    }

    @Data
    public static class Film {
        @Id
        private Long filmId;

        private List<Auffuehrung> auffuehrungen;
    }

    @Data
    public static class Auffuehrung {
        @Id
        private Long auffuehrungId;

        private List<Reservierung> reservierungen;
    }

    @Data
    public static class Reservierung {
        @Id
        private Long reservierungId;

        private List<Long> sitzplatzIds;
    }
}