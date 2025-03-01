package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommandRequest implements Serializable {

    @NonNull
    private CommandType operation;

    @NonNull
    private String entityType;

    private Object entity;

    public enum CommandType {
        CREATE_KINO,
        CREATE_KUNDE,
        CREATE_FILM,
        CREATE_AUFFUEHRUNG,
        CREATE_RESERVIERUNG,
        GET_KINO,
        GET_FILME,
        GET_AUFFUEHRUNGEN,
        GET_RESERVIERUNGEN,
        GET_KUNDEN,
        BOOK_RESERVIERUNG,
        CANCEL_RESERVIERUNG,
        DELETE_AUFFUEHRUNG,
        DELETE_KUNDE,
        DELETE_FILM,
        RESET
    }

}
