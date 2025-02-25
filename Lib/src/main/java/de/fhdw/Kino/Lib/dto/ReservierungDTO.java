package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record ReservierungDTO(Long reservierungId, List<Long> reservierungSitzplatzIds, Long reservierungAuffuehrungId, Long reservierungKundeId, ReservierungsStatusDTO reservierungStatus) implements Serializable {

    public enum ReservierungsStatusDTO {
        RESERVED,
        BOOKED,
        CANCELLED
    }
}
