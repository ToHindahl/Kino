package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Sitzreihe.class)
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long kundeId;
    @NotNull(message = "Vorname darf nicht leer sein.")
    private String vorname;
    @NotNull(message = "Nachname darf nicht leer sein.")
    private String nachname;
    @NotNull(message = "E-Mail darf nicht leer sein.")
    private String email;

    public KundeDTO toDTO() {
        return new KundeDTO(this.kundeId, this.vorname, this.nachname, this.email);
    }
}