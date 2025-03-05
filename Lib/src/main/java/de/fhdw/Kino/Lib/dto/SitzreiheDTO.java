package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SitzreiheDTO extends DTO {

    private Long sitzreiheId;

    private SitzreihenTypDTO sitzreihenTyp;

    private String bezeichnung;

    @JsonIgnore
    private KinosaalDTO kinosaal;

    private List<SitzplatzDTO> sitzplaetze = new ArrayList<>();

    private Long version;

    public enum SitzreihenTypDTO {
        LOGE_MIT_SERVICE(15.0),
        LOGE(10.0),
        PARKETT(7.50);

        public final Double price;
        SitzreihenTypDTO(Double price) {
            this.price = price;
        }
    }
}
