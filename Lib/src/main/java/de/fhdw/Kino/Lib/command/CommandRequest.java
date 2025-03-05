package de.fhdw.Kino.Lib.command;

import de.fhdw.Kino.Lib.dto.*;
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
    private CommandRequest.RequestEntityType requestEntityType;

    private DTO entity;

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

        RequestEntityType(Class<? extends DTO> dtOclass) {
            DTOclass = dtOclass;
        }
    }
}
