package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuffuehrungDTO extends DTO {

    private Long auffuehrungId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startzeit;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endzeit;

    private Long filmId;

    private Long kinosaalId;

    private Long version;
}
