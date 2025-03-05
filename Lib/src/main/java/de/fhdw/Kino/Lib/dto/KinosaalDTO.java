package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class KinosaalDTO extends DTO {

    private Long kinosaalId;

    private String name;

    @JsonIgnore
    private KinoDTO kino;

    private List<SitzreiheDTO> sitzreihen;

    private Long version;

    @Override
    public String toString() {
        return "KinosaalDTO{" +
                "kinosaalId=" + kinosaalId +
                ", name='" + name + '\'' +
                ", sitzreihen=" + (sitzreihen.size()) + " sitzreihen" +
                '}';
    }
}
