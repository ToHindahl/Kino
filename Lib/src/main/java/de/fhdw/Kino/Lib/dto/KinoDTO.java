package de.fhdw.Kino.Lib.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KinoDTO extends DTO {

    private Long kinoId;

    private String name;

    private List<KinosaalDTO> kinosaele = new ArrayList<>();

    private Long version;

    @Override
    public String toString() {
        return "KinoDTO{" +
                "kinoId=" + kinoId +
                ", name='" + name + '\'' +
                ", kinosaele=" + (kinosaele.size()) + " kinosaele" +
                '}';
    }
}
