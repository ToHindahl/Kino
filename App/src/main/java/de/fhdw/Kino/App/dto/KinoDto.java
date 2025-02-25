package de.fhdw.Kino.App.dto;

import de.fhdw.Kino.App.domain.Kinosaal;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KinoDto {
    private Long id;
    private String name;
    private List<Kinosaal> saele = new ArrayList<>();
}
