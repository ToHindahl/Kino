package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CrudRequest(Object entity, CrudOperation operation) implements Serializable {


    public enum CrudOperation {
        CREATE,
        READ
    }

}
