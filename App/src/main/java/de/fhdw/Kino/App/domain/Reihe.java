package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reihentyp darf nicht leer sein.")
    private ReihenTyp typ;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @NotNull(message = "Saal darf nicht leer sein.")
    private Kinosaal saal;

    @OneToMany(mappedBy = "reihe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Sitzplatz> sitze = new ArrayList<>();
}