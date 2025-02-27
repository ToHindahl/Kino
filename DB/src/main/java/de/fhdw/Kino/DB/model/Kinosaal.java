package de.fhdw.Kino.DB.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.KinosaalDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "kinosaalId",
        scope = Kinosaal.class
)
public class Kinosaal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long kinosaalId;

    @NotNull(message = "Name darf nicht leer sein.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "KINO_ID")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Kino darf nicht leer sein.")
    private Kino kino; // Umbenannt von 'kinosaalKino' zu 'kino'

    @OneToMany(mappedBy = "kinosaal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Sitzreihe> sitzreihen = new ArrayList<>();

    public KinosaalDTO toDTO() {
        return new KinosaalDTO(this.kinosaalId, this.name, this.kino.toDTO(), this.sitzreihen.stream().map(Sitzreihe::toDTO).toList());
    }
}