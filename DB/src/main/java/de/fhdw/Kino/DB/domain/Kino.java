package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "kinoId")
public class Kino {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long kinoId;

    @NotNull(message = "Name darf nicht leer sein.")
    private String name;

    @OneToMany(mappedBy = "kino", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Kinosaal> kinosaele = new ArrayList<>();

    public KinoDTO toDTO() {
        KinoDTO kinoDTO = new KinoDTO(this.kinoId, this.name, new ArrayList<>());

        this.getKinosaele().forEach(kinosaal -> {
            KinosaalDTO kinosaalDTO = new KinosaalDTO(kinosaal.getKinosaalId(), kinosaal.getName(), kinoDTO, new ArrayList<>());
            kinosaal.getSitzreihen().forEach(reihe -> {
                SitzreiheDTO sitzreiheDTO = new SitzreiheDTO(reihe.getSitzreiheId(), reihe.getSitzreihenTypDTO(), kinosaalDTO, new ArrayList<>());

                reihe.getSitzplaetze().forEach(sitzplatz -> {
                    SitzplatzDTO sitzplatzDTO = new SitzplatzDTO(sitzplatz.getSitzplatzId(), sitzplatz.getNummer(), sitzreiheDTO);
                    sitzreiheDTO.sitzplaetze().add(sitzplatzDTO);
                });
                kinosaalDTO.sitzreihen().add(sitzreiheDTO);
            });
            kinoDTO.kinosaele().add(kinosaalDTO);
        });

        return kinoDTO;
    }
}