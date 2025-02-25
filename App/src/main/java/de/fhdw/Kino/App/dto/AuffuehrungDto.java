package de.fhdw.Kino.App.dto;

import de.fhdw.Kino.App.domain.Film;
import de.fhdw.Kino.App.domain.Kinosaal;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuffuehrungDto {
    private Long id;
    private LocalDateTime startzeit;
    private Film film;
    private Kinosaal saal;
}
