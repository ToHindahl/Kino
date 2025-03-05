package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
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
