package de.fhdw.Kino.App.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReservierungRequest {
    private Long auffuehrungId;
    private Long kundeId;
    private List<Long> sitzplatzIds;
}