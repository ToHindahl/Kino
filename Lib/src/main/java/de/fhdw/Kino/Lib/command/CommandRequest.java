package de.fhdw.Kino.Lib.command;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommandRequest implements Serializable {

    private UUID transactionId;

    @NonNull
    private CommandRequest.Operation operation;

    @NonNull
    private String entityType;

    private Object entity;

    public enum Operation {
        CREATE,
        READ,
        READ_ALL,
        UPDATE,
        DELETE,
        COMMIT,
        BEGINN
    }
}
