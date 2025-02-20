package de.fhdw.Kino.Lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CrudAntwort {
    private String correlationId;
    private String status;
    private Map<String, Object> data;
}
