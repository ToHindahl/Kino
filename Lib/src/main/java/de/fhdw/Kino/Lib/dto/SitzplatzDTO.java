package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SitzplatzDTO extends DTO {

    private Long sitzplatzId;

    private int nummer;

    @JsonIgnore
    private SitzreiheDTO sitzreihe;

    private Long version;
}
