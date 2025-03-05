package de.fhdw.Kino.Lib.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmDTO extends DTO {

    private Long filmId;

    private String titel;

    private Long version;
}
