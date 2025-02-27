package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SitzplatzDTO implements Serializable {

    private Long sitzplatzId;

    @NonNull
    private int nummer;

    @NonNull
    @JsonIgnore
    private SitzreiheDTO sitzreihe;
}
