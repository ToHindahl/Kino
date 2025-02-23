package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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
    private ReihenTyp typ;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    private Kinosaal saal;

    @OneToMany(mappedBy = "reihe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sitzplatz> sitze = new ArrayList<>();
}