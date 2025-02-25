package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record ReiheDTO(Long reiheId, ReihenTypDTO reiheTyp, KinosaalDTO reiheSaal, List<SitzplatzDTO> reiheSitze) implements Serializable {

    public enum ReihenTypDTO {
        LOGE,
        PARKETT
    }
}
