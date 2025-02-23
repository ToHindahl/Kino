package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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
    private List<Long> sitzplatzIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "auffuehrung_id")
    private Auffuehrung auffuehrung;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "kunde_id")
    private Kunde kunde;

    @Enumerated(EnumType.STRING)
    private ReservierungsStatus status;
}