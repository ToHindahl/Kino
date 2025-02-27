package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Auffuehrung;
import de.fhdw.Kino.DB.domain.Kunde;
import de.fhdw.Kino.DB.domain.Reservierung;
import de.fhdw.Kino.DB.repositories.AuffuehrungRepository;
import de.fhdw.Kino.DB.repositories.KundeRepository;
import de.fhdw.Kino.DB.repositories.ReservierungRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservierungService {

    private final ReservierungRepository reservierungRepository;

    private final KundeRepository kundeRepository;

    private final AuffuehrungRepository auffuehrungRepository;

    @Transactional
    public CommandResponse handleReservierungCreation(ReservierungDTO dto) {
        Reservierung reservierung = new Reservierung();
        Optional<Kunde> kunde = kundeRepository.findById(dto.getKundeId());
        if(kunde.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kunde nicht gefunden", "error", null);
        }
        reservierung.setKunde(kunde.get());

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(dto.getAuffuehrungId());
        if(auffuehrung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Auffuehrung nicht gefunden", "error", null);
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

        reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);

        if(dto.getReservierungsStatus() == null) {
            reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
        } else {
            switch (dto.getReservierungsStatus()) {
                case RESERVED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
                case BOOKED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.BOOKED);
                case CANCELLED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.CANCELLED);
            }
        }

        reservierungRepository.save(reservierung);

        log.info("Reservierung erstellt: " + reservierung.toDTO());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created", "reservierung", reservierung.toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungCancelation(Long id) {

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
        List<ReservierungDTO> reservierungen = new ArrayList<>();
        reservierungRepository.findAll().forEach(reservierung -> reservierungen.add(reservierung.toDTO()));
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "reservierungsListe", reservierungen);
    }

}
