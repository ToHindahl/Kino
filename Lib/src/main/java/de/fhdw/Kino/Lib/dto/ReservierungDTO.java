package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservierungDTO implements Serializable {

    private Long reservierungId;

    @NonNull
    private List<Long> sitzplatzIds;

    @NonNull
    private Long auffuehrungId;

    @NonNull
    private Long kundeId;

    private ReservierungsStatusDTO reservierungsStatus;

    public enum ReservierungsStatusDTO {
        RESERVED,
        BOOKED,
        CANCELLED
    }
}
