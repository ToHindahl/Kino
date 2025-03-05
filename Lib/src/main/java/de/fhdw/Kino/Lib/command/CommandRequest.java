package de.fhdw.Kino.Lib.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.fhdw.Kino.Lib.dto.*;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@JsonDeserialize(using = CommandRequestDeserializer.class)
@NoArgsConstructor
public class CommandRequest implements Serializable {

    @NonNull
    private CommandRequest.Operation operation;

    @NonNull
    private CommandRequest.RequestEntityType requestEntityType;

    private Object entity;

    public CommandRequest(
            @NonNull Operation operation,
            @NonNull RequestEntityType requestEntityType,
            Object entity
    ) {
        this.operation = operation;
        this.requestEntityType = requestEntityType;

        if (entity != null && !requestEntityType.DTOclass.isInstance(entity)) {
            throw new IllegalArgumentException("Entity hat nicht den Typ " +
                    requestEntityType.DTOclass.getSimpleName());
        }

        this.entity = entity;
    }

    public CommandRequest(CommandRequest.Operation operation, CommandRequest.RequestEntityType requestEntityType) {
        this(operation, requestEntityType, null);
    }

    public enum Operation {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    public enum RequestEntityType implements EntityType {
        AUFFUEHRUNG(AuffuehrungDTO.class),
        KUNDE(KundeDTO.class),
        FILM(FilmDTO.class),
        KINO(KinoDTO.class),
        RESERVIERUNG(ReservierungDTO.class);

        public final Class<? extends DTO> DTOclass;

        RequestEntityType(Class<? extends DTO> DTOclass) {
            this.DTOclass = DTOclass;
        }
    }
}
