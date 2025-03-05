package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservierungDTO extends DTO {

    private Long reservierungId;

    private List<Long> sitzplatzIds;

    private Long auffuehrungId;

    private Long kundeId;

    private ReservierungsStatusDTO reservierungsStatus;

    private Long version;

    public enum ReservierungsStatusDTO {
        RESERVIERT,
        GEBUCHT,
        STORNIERT
    }
}
