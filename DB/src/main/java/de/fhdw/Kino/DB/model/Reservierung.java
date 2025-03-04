package de.fhdw.Kino.DB.model;

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

    @ElementCollection
    @NotEmpty(message = "Es muss mindestens ein Sitzplatz reserviert werden")
    private List<Long> sitzplatzIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "AUFFUEHRUNG_ID")
    @NotNull(message = "Aufführung darf nicht leer sein")
    private Auffuehrung auffuehrung;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "KUNDE_ID")
    @NotNull(message = "Kunde darf nicht leer sein")
    private Kunde kunde;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reservierungsstatus darf nicht leer sein")
    private ReservierungsStatus reservierungsStatus;

    @Version
    private Long version;

    public enum ReservierungsStatus {
        RESERVIERT,
        GEBUCHT,
        STORNIERT
    }

    public ReservierungsStatus getReservierungsStatusFromDTO(ReservierungDTO.ReservierungsStatusDTO reservierungsStatusDTO) {
        return switch (reservierungsStatusDTO) {
            case RESERVIERT -> ReservierungsStatus.RESERVIERT;
            case GEBUCHT -> ReservierungsStatus.GEBUCHT;
            case STORNIERT -> ReservierungsStatus.STORNIERT;
        };
    }

    public ReservierungDTO.ReservierungsStatusDTO getReservierungsStatusDTO() {
        return switch (reservierungsStatus) {
            case RESERVIERT -> ReservierungDTO.ReservierungsStatusDTO.RESERVIERT;
            case GEBUCHT -> ReservierungDTO.ReservierungsStatusDTO.GEBUCHT;
            case STORNIERT -> ReservierungDTO.ReservierungsStatusDTO.STORNIERT;
        };
    }

    public ReservierungDTO toDTO() {
        return new ReservierungDTO(this.reservierungId, this.sitzplatzIds, this.auffuehrung.getAuffuehrungId(),
                this.kunde.getKundeId(), this.getReservierungsStatusDTO(), this.getVersion());
    }
}

