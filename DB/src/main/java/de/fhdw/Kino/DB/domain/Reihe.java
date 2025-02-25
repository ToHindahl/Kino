package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.ReiheDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Reihe.class)
public class Reihe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reiheId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reihentyp darf nicht leer sein.")
    private ReihenTyp reiheTyp;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @NotNull(message = "Saal darf nicht leer sein.")
    private Kinosaal reiheSaal;

    @OneToMany(mappedBy = "sitzplatzReihe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Sitzplatz> reiheSitze = new ArrayList<>();

    public enum ReihenTyp {
        LOGE,
        PARKETT
    }

    public ReiheDTO.ReihenTypDTO getReiheTypDTO() {
        return switch (reiheTyp) {
            case LOGE -> ReiheDTO.ReihenTypDTO.LOGE;
            case PARKETT -> ReiheDTO.ReihenTypDTO.PARKETT;
        };
    }

}