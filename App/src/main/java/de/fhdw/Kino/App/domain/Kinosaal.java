package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
        property = "id",
        scope = Kinosaal.class
)
public class Kinosaal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Name darf nicht leer sein.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "kino_id")
    // Wenn du hier nur die ID möchtest, kann folgender Zusatz helfen:
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Kino darf nicht leer sein.")
    private Kino kino;

    @OneToMany(mappedBy = "saal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Reihe> reihen = new ArrayList<>();
}