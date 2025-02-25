package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "kinoId")
public class Kino {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long kinoId;

    @NotNull(message = "Name darf nicht leer sein.")
    private String kinoName;

    @OneToMany(mappedBy = "kino", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Kinosaal> kinoSaele = new ArrayList<>();
}