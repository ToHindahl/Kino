package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Reihe.class)
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Vorname darf nicht leer sein.")
    private String vorname;
    @NotNull(message = "Nachname darf nicht leer sein.")
    private String nachname;
    @NotNull(message = "E-Mail darf nicht leer sein.")
    private String email;
}