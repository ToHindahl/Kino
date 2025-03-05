package de.fhdw.Kino.Lib.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.fhdw.Kino.Lib.dto.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@JsonDeserialize(using = CommandResponseDeserializer.class)
public class CommandResponse implements Serializable {

    @NonNull
    private CommandStatus status;

    @NonNull
    private String message;

    @NonNull
    private CommandResponse.ResponseEntityType responseEntityType;

    private Object entity;

    public CommandResponse(CommandStatus status, String message, CommandResponse.ResponseEntityType responseEntityType, Object entity) {
        this.status = status;
        this.message = message;
        this.responseEntityType = responseEntityType;

        switch (responseEntityType) {
            case AUFFUEHRUNG, KUNDE, FILM, KINO, RESERVIERUNG -> {
                if (!responseEntityType.DTOclass.isInstance(entity)) {
                    throw new IllegalArgumentException("Entity muss den Typ besitzen: " + responseEntityType.DTOclass.getSimpleName());
                }
            }
            case AUFFUEHRUNGSLISTE, KUNDENLISTE, RESERVIERUNGSLISTE, FILMLISTE -> {
                if (!(entity instanceof List)) {
                    throw new IllegalArgumentException("Entity muss den Typ besitzen: " + List.class.getSimpleName());
                }
            }
            case EMPTY -> {
                if (entity != null) {
                    throw new IllegalArgumentException("Entity muss null sein");
                }
            }
        }

        this.entity = entity;
    }

    public CommandResponse(CommandStatus status, String message, CommandResponse.ResponseEntityType responseEntityType) {
        this(status, message, responseEntityType, null);
    }

    public enum CommandStatus {
        SUCCESS, ERROR
    }

    public enum ResponseEntityType implements EntityType {
        AUFFUEHRUNG(AuffuehrungDTO.class),
        AUFFUEHRUNGSLISTE(AuffuehrungDTO.class),
        KUNDE(KundeDTO.class),
        KUNDENLISTE(KundeDTO.class),
        FILM(FilmDTO.class),
        FILMLISTE(FilmDTO.class),
        KINO(KinoDTO.class),
        RESERVIERUNG(ReservierungDTO.class),
        RESERVIERUNGSLISTE(ReservierungDTO.class),
        EMPTY(null);

        public final Class<? extends DTO> DTOclass;

        ResponseEntityType(Class<? extends DTO> DTOclass) {
            this.DTOclass = DTOclass;
        }
    }
}
