package de.fhdw.Kino.DB.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Film.class
)
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long filmId;

    @NotNull(message = "Titel darf nicht leer sein.")
    private String titel;

    @Version
    private Long version;

    public FilmDTO toDTO() {
        return new FilmDTO(this.filmId, this.titel, this.getVersion());
    }
}