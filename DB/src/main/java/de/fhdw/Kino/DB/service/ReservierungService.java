package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Auffuehrung;
import de.fhdw.Kino.DB.model.Kunde;
import de.fhdw.Kino.DB.model.Reservierung;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.DB.repository.KundeRepository;
import de.fhdw.Kino.DB.repository.ReservierungRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservierungService {

    private final ReservierungRepository reservierungRepository;

    private final KundeRepository kundeRepository;

    private final AuffuehrungRepository auffuehrungRepository;

    private final KinoRepository kinoRepository;

    @Transactional
    public CommandResponse handleReservierungCreation(ReservierungDTO dto) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Reservierung reservierung = new Reservierung();
        Optional<Kunde> kunde = kundeRepository.findById(dto.getKundeId());
        if(kunde.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kunde nicht gefunden", "error", null);
        }
        reservierung.setKunde(kunde.get());

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(dto.getAuffuehrungId());
        if(auffuehrung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Aufführung nicht gefunden", "error", null);
        }

        if(auffuehrung.get().getStartzeit().isAfter(LocalDateTime.now())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Aufführung bereits gestartet", "error", null);
        }

        reservierung.setAuffuehrung(auffuehrung.get());

        ArrayList<Long> alleSitzplaetze = new ArrayList<>();

        auffuehrung.get().getKinosaal().getSitzreihen().forEach(reihe -> {
            reihe.getSitzplaetze().forEach(sitzplatz -> {
                if(dto.getSitzplatzIds().contains(sitzplatz.getSitzplatzId())) {
                    alleSitzplaetze.add(sitzplatz.getSitzplatzId());
                }
            });
        });

        ArrayList<Long> reservierteSitzplaetze = new ArrayList<>();
        reservierungRepository.findAll().stream().filter(r -> r.getAuffuehrung() == auffuehrung.get() && !(r.getReservierungsStatusDTO().equals(ReservierungDTO.ReservierungsStatusDTO.CANCELLED))).forEach(r -> {
            reservierteSitzplaetze.addAll(r.getSitzplatzIds());
        });

        alleSitzplaetze.removeIf(reservierteSitzplaetze::contains);

        if(!alleSitzplaetze.containsAll(dto.getSitzplatzIds())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Sitzplatz nicht verfügbar", "error", null);
        }

        reservierung.setSitzplatzIds(dto.getSitzplatzIds());

        if(dto.getReservierungsStatus() == null) {
            reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
        } else {
            switch (dto.getReservierungsStatus()) {
                case RESERVED -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
                case BOOKED -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.BOOKED);
                case CANCELLED -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.CANCELLED);
            }
        }

        reservierungRepository.save(reservierung);

        log.info("Reservierung erstellt: " + reservierung.toDTO());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created", "reservierung", reservierung.toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungCancelation(Long id) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Optional<Reservierung> reservierung = reservierungRepository.findById(id);

        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung nicht gefunden", "error", null);
        }

        if(reservierung.get().getReservierungsStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung kann nicht storniert werden", "error", null);
        }

        reservierung.get().setReservierungsStatus(Reservierung.ReservierungsStatus.CANCELLED);

        reservierungRepository.save(reservierung.get());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "canceled", "reservierung", reservierung.get().toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungBooking(Long id) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Optional<Reservierung> reservierung = reservierungRepository.findById(id);

        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung nicht gefunden", "error", null);
        }

        if(reservierung.get().getReservierungsStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung kann nicht gebucht werden", "error", null);
        }

        reservierung.get().setReservierungsStatus(Reservierung.ReservierungsStatus.BOOKED);

        reservierungRepository.save(reservierung.get());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "booked", "reservierung", reservierung.get().toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungRequestAll() {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "reservierungsListe", reservierungRepository.findAll().stream().map(Reservierung::toDTO).toList());
    }

}
