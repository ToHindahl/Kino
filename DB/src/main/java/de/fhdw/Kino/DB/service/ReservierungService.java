package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Auffuehrung;
import de.fhdw.Kino.DB.model.Kunde;
import de.fhdw.Kino.DB.model.Reservierung;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.KundeRepository;
import de.fhdw.Kino.DB.repository.ReservierungRepository;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        Kunde kunde = kundeRepository.findById(dto.getKundeId()).get();
        reservierung.setKunde(kunde);

        Auffuehrung auffuehrung = auffuehrungRepository.findById(dto.getAuffuehrungId()).get();
        reservierung.setAuffuehrung(auffuehrung);

        reservierung.setSitzplatzIds(dto.getSitzplatzIds());

        if(dto.getReservierungsStatus() == null) {
            reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVIERT);
        } else {
            switch (dto.getReservierungsStatus()) {
                case RESERVIERT -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.RESERVIERT);
                case GEBUCHT -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.GEBUCHT);
                case STORNIERT -> reservierung.setReservierungsStatus(Reservierung.ReservierungsStatus.STORNIERT);
            }
        }

        reservierungRepository.save(reservierung);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created", "reservierung", reservierung.toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungUpdate(ReservierungDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.getReservierungId());
        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", "null");
        }

        if(!dto.getVersion().equals(reservierung.get().getVersion())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "version mismatch", "null");
        }

        reservierung.get().setReservierungsStatus(reservierung.get().getReservierungsStatusFromDTO(dto.getReservierungsStatus()));
        reservierungRepository.save(reservierung.get());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "updated", "reservierung", reservierung.get().toDTO());
    }

    @Transactional
    public CommandResponse handleReservierungRequest(Long id) {
        Optional<Reservierung> reservierung = reservierungRepository.findById(id);
        return reservierung.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "reservierung", value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", "null"));
    }

    @Transactional
    public CommandResponse handleReservierungRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "reservierungsListe", reservierungRepository.findAll().stream().map(Reservierung::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleReservierungDeletion(ReservierungDTO dto) {

        Optional<Reservierung> reservierung = reservierungRepository.findById(dto.getReservierungId());
        if(reservierung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", "null");
        }

        if(!dto.getVersion().equals(reservierung.get().getVersion())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "version mismatch", "null");
        }

        reservierungRepository.deleteById(dto.getReservierungId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"deleted", "null");
    }

}
