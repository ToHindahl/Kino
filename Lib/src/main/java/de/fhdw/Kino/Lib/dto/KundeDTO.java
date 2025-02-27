package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KundeDTO  implements Serializable {

    private Long kundeId;

    @NonNull
    private String vorname;

    @NonNull
    private String nachname;

    @NonNull
    private String email;
}
