package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmDTO implements Serializable {

    private Long filmId;

    @NonNull
    private String titel;

    private Long version;
}
