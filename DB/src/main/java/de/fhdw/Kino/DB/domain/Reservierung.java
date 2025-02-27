package de.fhdw.Kino.DB.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.fhdw.Kino.Lib.dto.ReservierungDTO;
import jakarta.persistence.*;
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
    private Long reservierungId;

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
    @NotNull(message = "Reservierungsstatus darf nicht leer sein.")
    private ReservierungsStatus reservierungsStatus;

    public enum ReservierungsStatus {
        RESERVED,
        BOOKED,
        CANCELLED
    }

    public ReservierungDTO.ReservierungsStatusDTO getReservierungsStatusDTO() {
        return switch (reservierungsStatus) {
            case RESERVED -> ReservierungDTO.ReservierungsStatusDTO.RESERVED;
            case BOOKED -> ReservierungDTO.ReservierungsStatusDTO.BOOKED;
            case CANCELLED -> ReservierungDTO.ReservierungsStatusDTO.CANCELLED;
        };
    }

    public ReservierungDTO toDTO() {
        return new ReservierungDTO(this.reservierungId, this.sitzplatzIds, this.auffuehrung.getAuffuehrungId(), this.kunde.getKundeId(), this.getReservierungsStatusDTO());
    }
}

