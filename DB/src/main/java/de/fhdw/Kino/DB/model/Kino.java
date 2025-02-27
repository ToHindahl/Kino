package de.fhdw.Kino.DB.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

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
                SitzreiheDTO sitzreiheDTO = new SitzreiheDTO(reihe.getSitzreiheId(), reihe.getSitzreihenTypDTO(), reihe.getBezeichnung(), kinosaalDTO, new ArrayList<>());

                reihe.getSitzplaetze().forEach(sitzplatz -> {
                    SitzplatzDTO sitzplatzDTO = new SitzplatzDTO(sitzplatz.getSitzplatzId(), sitzplatz.getNummer(), sitzreiheDTO);
                    sitzreiheDTO.getSitzplaetze().add(sitzplatzDTO);
                });
                kinosaalDTO.getSitzreihen().add(sitzreiheDTO);
            });
            kinoDTO.getKinosaele().add(kinosaalDTO);
        });

        return kinoDTO;
    }
}