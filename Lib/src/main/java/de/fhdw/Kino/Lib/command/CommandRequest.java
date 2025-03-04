package de.fhdw.Kino.Lib.command;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommandRequest implements Serializable {

    @NonNull
    private CommandRequest.Operation operation;

    @NonNull
    private String entityType;

    private Object entity;

    public enum Operation {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }
}
