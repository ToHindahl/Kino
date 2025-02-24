package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Auffuehrung.class
)
public class Auffuehrung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Startzeit darf nicht leer sein.")
    private LocalDateTime startzeit;

    @ManyToOne
    @JoinColumn(name = "film_id")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Film-ID darf nicht leer sein.")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Saal darf nicht leer sein.")
    private Kinosaal saal;
}