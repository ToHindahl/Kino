package de.fhdw.Kino.DB.listener;

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
public class ReservierungListener {

    private final ReservierungRepository reservierungRepository;

    private final KundeRepository kundeRepository;

    private final AuffuehrungRepository auffuehrungRepository;

    private final KinoRepository kinoRepository;

    @Transactional
    @RabbitListener(queues = "reservierung.create.queue")
    public CreationResponseDTO handleReservierungCreation(ReservierungDTO dto) {
        Reservierung reservierung = new Reservierung();
        Optional<Kunde> kunde = kundeRepository.findById(dto.reservierungKundeId());
        if(kunde.isEmpty()) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Kunde nicht gefunden");
        }
        reservierung.setReservierungKunde(kunde.get());

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(dto.reservierungAuffuehrungId());
        if(auffuehrung.isEmpty()) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Auffuehrung nicht gefunden");
        }

        reservierung.setReservierungAuffuehrung(auffuehrung.get());

        ArrayList<Long> alleSitzplaetze = new ArrayList<>();

        auffuehrung.get().getAuffuehrungSaal().getKinosaalReihen().forEach(reihe -> {
            reihe.getReiheSitze().forEach(sitzplatz -> {
                if(dto.reservierungSitzplatzIds().contains(sitzplatz.getSitzplatzId())) {
                    alleSitzplaetze.add(sitzplatz.getSitzplatzId());
                }
            });
        });

        ArrayList<Long> reservierteSitzplaetze = new ArrayList<>();
        reservierungRepository.findAll().stream().filter(r -> r.getReservierungAuffuehrung() == auffuehrung.get()).forEach(r -> {
            reservierteSitzplaetze.addAll(r.getReservierungSitzplatzIds());
        });

        alleSitzplaetze.removeIf(reservierteSitzplaetze::contains);


        if(!alleSitzplaetze.containsAll(dto.reservierungSitzplatzIds())) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Sitzplatz nicht verfügbar");
        }

        reservierung.setReservierungSitzplatzIds(dto.reservierungSitzplatzIds());

        reservierung.setReservierungStatus(Reservierung.ReservierungsStatus.RESERVED);

        if(dto.reservierungStatus() == null) {
            reservierung.setReservierungStatus(Reservierung.ReservierungsStatus.RESERVED);
        } else {
            switch (dto.reservierungStatus()) {
                case RESERVED:
                    reservierung.setReservierungStatus(Reservierung.ReservierungsStatus.RESERVED);
                case BOOKED:
                    reservierung.setReservierungStatus(Reservierung.ReservierungsStatus.BOOKED);
                case CANCELLED:
                    reservierung.setReservierungStatus(Reservierung.ReservierungsStatus.CANCELLED);
            }
        }

        reservierungRepository.save(reservierung);

        return new CreationResponseDTO(reservierung.getReservierungId(), StatusDTO.SUCCESS, "created");
    }

    @Transactional
    @RabbitListener(queues = "reservierung.cancel.queue")
    public CancelReservierungResponseDTO handleReservierungCancelation(CancelReservierungRequestDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.reservierungId());

        if(reservierung.isEmpty()) {
            return new CancelReservierungResponseDTO(-1L, StatusDTO.ERROR, "Reservierung nicht gefunden");
        }

        if(reservierung.get().getReservierungStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new CancelReservierungResponseDTO(-1L, StatusDTO.ERROR, "Reservierung kann nicht storniert werden");
        }

        reservierung.get().setReservierungStatus(Reservierung.ReservierungsStatus.CANCELLED);

        reservierungRepository.save(reservierung.get());

        return new CancelReservierungResponseDTO(reservierung.get().getReservierungId(), StatusDTO.SUCCESS, "cancelled");
    }

    @Transactional
    @RabbitListener(queues = "reservierung.book.queue")
    public BookReservierungResponseDTO handleReservierungBooking(BookReservierungRequestDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.reservierungId());

        if(reservierung.isEmpty()) {
            return new BookReservierungResponseDTO(-1L, StatusDTO.ERROR, "Reservierung nicht gefunden");
        }

        if(reservierung.get().getReservierungStatus() != Reservierung.ReservierungsStatus.RESERVED) {
            return new BookReservierungResponseDTO(-1L, StatusDTO.ERROR, "Reservierung kann nicht gebucht werden");
        }

        reservierung.get().setReservierungStatus(Reservierung.ReservierungsStatus.BOOKED);

        reservierungRepository.save(reservierung.get());

        return new BookReservierungResponseDTO(reservierung.get().getReservierungId(), StatusDTO.SUCCESS, "booked");
    }
}
