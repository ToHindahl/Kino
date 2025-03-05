package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KinosaalDTO extends DTO {

    private Long kinosaalId;

    private String name;

    @JsonIgnore
    private KinoDTO kino;

    private List<SitzreiheDTO> sitzreihen = new ArrayList<>();

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
