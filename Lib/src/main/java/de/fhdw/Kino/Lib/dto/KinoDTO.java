package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class KinoDTO implements Serializable {

    private Long kinoId;

    @NonNull
    private String name;

    @NonNull
    private List<KinosaalDTO> kinosaele;

    private Long version;

    @Override
    public String toString() {
        return "KinoDTO{" +
                "kinoId=" + kinoId +
                ", name='" + name + '\'' +
                ", kinosaele=" + (kinosaele != null ? kinosaele.size() : 0) + " kinosaele" +
                '}';
    }

}
