package de.fhdw.Kino.App.dto;

import lombok.Data;


@Data
public class KundeDto {
    private Long id;
    private String vorname;
    private String nachname;
    private String email;
}
