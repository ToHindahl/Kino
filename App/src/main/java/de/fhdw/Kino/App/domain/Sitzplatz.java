package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private Long id;

    @Min(value = 1, message = "Die Nummer muss mindestens 1 sein.")
    private int nummer;

    @ManyToOne
    @JoinColumn(name = "reihe_id")
    @NotNull(message = "Reihe darf nicht leer sein.")
    private Reihe reihe;
}