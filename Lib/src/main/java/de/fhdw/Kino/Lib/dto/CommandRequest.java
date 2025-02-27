package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;


public record CommandRequest(CommandType operation, String entityType, Object entity) implements Serializable {


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
        CANCEL_RESERVIERUNG
    }

}
