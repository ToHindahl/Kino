package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class KinosaalDTO implements Serializable {

    private Long kinosaalId;

    @NonNull
    private String name;

    @JsonIgnore
    @NonNull
    private KinoDTO kino;

    @NonNull
    private List<SitzreiheDTO> sitzreihen;

    private Long version;

    @Override
    public String toString() {
        return "KinosaalDTO{" +
                "kinosaalId=" + kinosaalId +
                ", name='" + name + '\'' +
                ", sitzreihen=" + (sitzreihen != null ? sitzreihen.size() : 0) + " sitzreihen" +
                '}';
    }


}
