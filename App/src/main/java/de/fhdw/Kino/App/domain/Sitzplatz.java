package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Sitzplatz.class)
public class Sitzplatz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int nummer;

    @ManyToOne
    @JoinColumn(name = "reihe_id")
    private Reihe reihe;
}