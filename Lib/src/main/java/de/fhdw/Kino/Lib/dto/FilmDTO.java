package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmDTO extends DTO {

    private Long filmId;

    private String titel;

    private Long version;
}
