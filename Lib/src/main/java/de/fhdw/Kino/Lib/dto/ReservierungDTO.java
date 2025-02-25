package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record ReservierungDTO(Long reservierungId, List<Long> reservierungSitzplatzIds, AuffuehrungDTO reservierungAuffuehrung, KundeDTO reservierungKunde, ReservierungsStatusDTO reservierungStatus) implements Serializable {

    public enum ReservierungsStatusDTO {
        RESERVED,
        BOOKED,
        CANCELLED
    }
}
