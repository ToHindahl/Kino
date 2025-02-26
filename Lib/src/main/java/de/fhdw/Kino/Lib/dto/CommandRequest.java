package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CommandRequest(CommandType operation, Object entity) implements Serializable {


    public enum CommandType {
        CREATE_KINO,
        CREATE_KUNDE,
        CREATE_FILM,
        CREATE_AUFFUEHRUNG,
        CREATE_RESERVIERUNG,
        GET_KINO,
        GET_FILME,
        GET_AUFFUEHRUNGEN,
        BOOK_RESERVIERUNG,
        CANCEL_RESERVIERUNG
    }

}
