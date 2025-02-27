package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommandResponse implements Serializable {

    @NonNull
    private CommandStatus status;

    @NonNull
    private String message;

    @NonNull
    private String entityType;

    private Object entity;

    public enum CommandStatus {
        SUCCESS, ERROR
    }
}
