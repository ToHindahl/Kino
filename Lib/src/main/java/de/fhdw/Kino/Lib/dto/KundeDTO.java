package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KundeDTO extends DTO {

    private Long kundeId;

    private String vorname;

    private String nachname;

    private String email;

    private Long version;
}
