package de.fhdw.Kino.App.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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

    private LocalDateTime startzeit;

    @ManyToOne
    @JoinColumn(name = "film_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Kinosaal saal;
}