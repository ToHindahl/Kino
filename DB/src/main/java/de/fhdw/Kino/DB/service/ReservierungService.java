package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Auffuehrung;
import de.fhdw.Kino.DB.domain.Kunde;
import de.fhdw.Kino.DB.domain.Reservierung;
import de.fhdw.Kino.DB.repositories.AuffuehrungRepository;
import de.fhdw.Kino.DB.repositories.KinoRepository;
import de.fhdw.Kino.DB.repositories.KundeRepository;
import de.fhdw.Kino.DB.repositories.ReservierungRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservierungService {

    private final ReservierungRepository reservierungRepository;

    private final KundeRepository kundeRepository;

    private final AuffuehrungRepository auffuehrungRepository;

    @Transactional
    public CommandResponse handleReservierungCreation(ReservierungDTO dto) {
        Reservierung reservierung = new Reservierung();
        Optional<Kunde> kunde = kundeRepository.findById(dto.kundeId());
        if(kunde.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kunde nicht gefunden", null);
        }
        reservierung.setKunde(kunde.get());

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(dto.auffuehrungId());
        if(auffuehrung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Auffuehrung nicht gefunden", null);
        }

        reservierung.setAuffuehrung(auffuehrung.get());

        ArrayList<Long> alleSitzplaetze = new ArrayList<>();

        auffuehrung.get().getKinosaal().getSitzreihen().forEach(reihe -> {
            reihe.getSitzplaetze().forEach(sitzplatz -> {
                if(dto.sitzplatzIds().contains(sitzplatz.getSitzplatzId())) {
                    alleSitzplaetze.add(sitzplatz.getSitzplatzId());
                }
            });
        });

        ArrayList<Long> reservierteSitzplaetze = new ArrayList<>();
        reservierungRepository.findAll().stream().filter(r -> r.getAuffuehrung() == auffuehrung.get()).forEach(r -> {
            reservierteSitzplaetze.addAll(r.getSitzplatzIds());
        });

        alleSitzplaetze.removeIf(reservierteSitzplaetze::contains);


        if(!alleSitzplaetze.containsAll(dto.sitzplatzIds())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Sitzplatz nicht verfügbar", null);
        }

        reservierung.setSitzplatzIds(dto.sitzplatzIds());

        reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);

        if(dto.reservierungsStatus() == null) {
            reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
        } else {
            switch (dto.reservierungsStatus()) {
                case RESERVED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVED);
                case BOOKED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.BOOKED);
                case CANCELLED:
                    reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.CANCELLED);
            }
        }

        reservierungRepository.save(reservierung);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created", reservierung.toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungCancelation(CancelReservierungRequestDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.reservierungId());

        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung nicht gefunden", null);
        }

        if(reservierung.get().getReservierungsStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung kann nicht storniert werden", null);
        }

        reservierung.get().setReservierungsStatus(Reservierung.ReservierungsStatus.CANCELLED);

        reservierungRepository.save(reservierung.get());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "canceled", reservierung.get().toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungBooking(BookReservierungRequestDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.reservierungId());

        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung nicht gefunden", null);
        }

        if(reservierung.get().getReservierungsStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Reservierung kann nicht gebucht werden", null);
        }

        reservierung.get().setReservierungsStatus(Reservierung.ReservierungsStatus.BOOKED);

        reservierungRepository.save(reservierung.get());

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "booked", reservierung.get().toDTO());
    }
}
