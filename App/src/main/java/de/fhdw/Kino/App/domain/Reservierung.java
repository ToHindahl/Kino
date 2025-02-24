package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reservierung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Speicherung der Sitzplatz-IDs zur Entkopplung
    @ElementCollection
    @NotEmpty(message = "Es muss mindestens ein Sitzplatz reserviert werden.")
    private List<Long> sitzplatzIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "auffuehrung_id")
    @NotNull(message = "Aufführung darf nicht leer sein.")
    private Auffuehrung auffuehrung;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "kunde_id")
    @NotNull(message = "Kunde darf nicht leer sein.")
    private Kunde kunde;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status darf nicht leer sein.")
    private ReservierungsStatus status;
}