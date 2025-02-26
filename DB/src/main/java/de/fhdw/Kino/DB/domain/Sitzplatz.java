package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.SitzplatzDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Sitzplatz.class)
public class Sitzplatz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sitzplatzId;

    @Min(value = 1, message = "Die Nummer muss mindestens 1 sein.")
    private int nummer;

    @ManyToOne
    @JoinColumn(name = "reihe_id")
    @NotNull(message = "Sitzreihe darf nicht leer sein.")
    private Sitzreihe sitzreihe;

    public SitzplatzDTO toDTO() {
        return new SitzplatzDTO(this.sitzplatzId, this.nummer, this.sitzreihe.toDTO());
    }
}