package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.SitzreiheDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Sitzreihe.class)
public class Sitzreihe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sitzreiheId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reihentyp darf nicht leer sein.")
    private SitzreihenTyp sitzreihenTyp;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @NotNull(message = "Kinosaal darf nicht leer sein.")
    private Kinosaal kinosaal;

    @OneToMany(mappedBy = "sitzreihe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Sitzplatz> sitzplaetze = new ArrayList<>();

    public enum SitzreihenTyp {
        LOGE_MIT_SERVICE,
        LOGE,
        PARKETT
    }

    public SitzreiheDTO.SitzreihenTypDTO getSitzreihenTypDTO() {
        return switch (sitzreihenTyp) {
            case LOGE_MIT_SERVICE -> SitzreiheDTO.SitzreihenTypDTO.LOGE_MIT_SERVICE;
            case LOGE -> SitzreiheDTO.SitzreihenTypDTO.LOGE;
            case PARKETT -> SitzreiheDTO.SitzreihenTypDTO.PARKETT;
        };
    }

    public SitzreiheDTO toDTO() {
        return new SitzreiheDTO(this.sitzreiheId, this.getSitzreihenTypDTO(), this.kinosaal.toDTO(), this.sitzplaetze.stream().map(s -> s.toDTO()).toList());
    }
}