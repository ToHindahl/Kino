package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CommandResponse(CommandStatus status, String message, String entityType, Object entity) implements Serializable {
    public enum CommandStatus {
        SUCCESS, ERROR
    }
}
