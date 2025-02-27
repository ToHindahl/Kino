package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record SitzreiheDTO(Long sitzreiheId, SitzreihenTypDTO sitzreihenTyp, KinosaalDTO kinosaal, List<SitzplatzDTO> sitzplaetze) implements Serializable {

    public enum SitzreihenTypDTO {
        LOGE_MIT_SERVICE,
        LOGE,
        PARKETT,
    }
}
