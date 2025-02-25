package de.fhdw.Kino.DB.domain;

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
    private Long kundeId;
    @NotNull(message = "Vorname darf nicht leer sein.")
    private String kundeVorname;
    @NotNull(message = "Nachname darf nicht leer sein.")
    private String kundeNachname;
    @NotNull(message = "E-Mail darf nicht leer sein.")
    private String kundeEmail;
}