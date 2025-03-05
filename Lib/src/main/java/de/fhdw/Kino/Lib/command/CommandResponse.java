package de.fhdw.Kino.Lib.command;

import de.fhdw.Kino.Lib.dto.*;
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
    private CommandResponse.ResponseEntityType responseEntityType;

    private Object entity;

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

        ResponseEntityType(Class<? extends DTO> dtOclass) {
            DTOclass = dtOclass;
        }
    }
}
