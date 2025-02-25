package de.fhdw.Kino.DB.domain;

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
    private Long auffuehrungId;

    @NotNull(message = "Startzeit darf nicht leer sein.")
    private LocalDateTime auffuehrungStartzeit;

    @ManyToOne
    @JoinColumn(name = "film_id")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Film-ID darf nicht leer sein.")
    private Film auffuehrungFilm;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Saal darf nicht leer sein.")
    private Kinosaal auffuehrungSaal;
}