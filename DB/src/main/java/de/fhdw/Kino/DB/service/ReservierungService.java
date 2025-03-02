package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Auffuehrung;
import de.fhdw.Kino.DB.model.Kunde;
import de.fhdw.Kino.DB.model.Reservierung;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.DB.repository.KundeRepository;
import de.fhdw.Kino.DB.repository.ReservierungRepository;
import de.fhdw.Kino.Lib.command.CommandResponse;
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

        Reservierung reservierung = new Reservierung();
        Kunde kunde = kundeRepository.findById(dto.getKundeId()).get();
        reservierung.setKunde(kunde);

        Auffuehrung auffuehrung = auffuehrungRepository.findById(dto.getAuffuehrungId()).get();
        reservierung.setAuffuehrung(auffuehrung);

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
    public CommandResponse handleReservierungUpdate(ReservierungDTO dto) {

        Reservierung reservierung = reservierungRepository.findById(dto.getReservierungId()).get();
        reservierung.setReservierungsStatus(reservierung.getReservierungsStatusFromDTO(dto.getReservierungsStatus()));
        reservierungRepository.save(reservierung);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "updated", "reservierung", reservierung.toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungRequest(Long id) {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "reservierung", reservierungRepository.findById(id).get().toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "reservierungsListe", reservierungRepository.findAll().stream().map(Reservierung::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleReservierungDeletion(ReservierungDTO dto) {
        reservierungRepository.deleteById(dto.getReservierungId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"deleted", "null");
    }

}
