package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SitzreiheDTO implements Serializable {

    private Long sitzreiheId;

    @NonNull
    private SitzreihenTypDTO sitzreihenTyp;

    @NonNull
    private String bezeichnung;

    @NonNull
    @JsonIgnore
    private KinosaalDTO kinosaal;

    @NonNull
    private List<SitzplatzDTO> sitzplaetze;

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
