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
    @JsonIgnore
    private KinosaalDTO kinosaal;

    @NonNull
    private List<SitzplatzDTO> sitzplaetze;

    public enum SitzreihenTypDTO {
        LOGE_MIT_SERVICE,
        LOGE,
        PARKETT,
    }
}
