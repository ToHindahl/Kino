package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record ReservierungDTO(Long reservierungId, List<Long> sitzplatzIds, Long auffuehrungId, Long kundeId, ReservierungsStatusDTO reservierungsStatus) implements Serializable {

    public enum ReservierungsStatusDTO {
        RESERVED,
        BOOKED,
        CANCELLED
    }
}
