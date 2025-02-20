package de.fhdw.Kino.Lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrudAnfrage {
    private String operation; // "CREATE", "READ", "UPDATE", "DELETE"
    private String entityType; // "Movie", "User", etc.
    private Map<String, Object> data; // JSON-Daten für das Objekt
    private String correlationId; // Korrelation für Antwort
    private String replyTo; // Antwort-Queue
}
