package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CrudResponse(Long id, Status status, String message) implements Serializable {
    public enum Status {
        SUCCESS, ERROR
    }
}
