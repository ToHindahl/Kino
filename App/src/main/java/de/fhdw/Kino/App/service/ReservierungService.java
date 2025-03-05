package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservierungService {

    private final CommandProducer commandProducer;

    @Transactional
    public ReservierungDTO createReservierung(ReservierungDTO dto) {

        if(dto.getSitzplatzIds().isEmpty()) {
            throw new RuntimeException("Keine Sitzplätze ausgewählt");
        }

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        KinoDTO kino = (KinoDTO) kinoResponse.getEntity();

        CommandResponse auffuehrungResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.AUFFUEHRUNG, new AuffuehrungDTO(dto.getAuffuehrungId(), null, null, null, null, null)));
        if (auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && auffuehrungResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Aufführung nicht gefunden");
        } else if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungResponse.getMessage());
        }

        AuffuehrungDTO auffuehrung = (AuffuehrungDTO) auffuehrungResponse.getEntity();
        if (auffuehrung.getStartzeit().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Aufführung noch nicht gestartet");
        }

        CommandResponse kundeResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KUNDE, new KundeDTO(dto.getKundeId(), null, null, null, null)));
        if (kundeResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kundeResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kunde nicht gefunden");
        } else if(kundeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kundeResponse.getMessage());
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO()));
        if(reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        List<SitzplatzDTO> alleSitzplaetze = new ArrayList<>();

        kino.getKinosaele().stream().filter(s -> s.getKinosaalId().equals(auffuehrung.getKinosaalId())).findFirst().get().getSitzreihen().forEach(reihe -> {
            alleSitzplaetze.addAll(reihe.getSitzplaetze());
        });

        List<Long> reservierteSitzplaetze = new ArrayList<>();
        reservierungen.stream().filter(r -> r.getAuffuehrungId().equals(auffuehrung.getAuffuehrungId()) && !(r.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.STORNIERT))).forEach(r -> {
            reservierteSitzplaetze.addAll(r.getSitzplatzIds());
        });

        alleSitzplaetze.removeIf(s -> reservierteSitzplaetze.contains(s.getSitzplatzId()));
        if(!new HashSet<>(alleSitzplaetze.stream().map(SitzplatzDTO::getSitzplatzId).toList()).containsAll(dto.getSitzplatzIds())) {
            throw new RuntimeException("Sitzplatz nicht verfügbar");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, CommandRequest.RequestEntityType.RESERVIERUNG, dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (ReservierungDTO) response.getEntity();
    }

    @Transactional
    public ReservierungDTO bookReservierung(Long id) {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse reservierungResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO(id, null, null, null, null, null)));
        if (reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && reservierungResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Reservierung nicht gefunden");
        } else if(reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungResponse.getMessage());
        }

        ReservierungDTO reservierung = (ReservierungDTO) reservierungResponse.getEntity();
        if(reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.GEBUCHT || reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.STORNIERT) {
            throw new RuntimeException("Reservierung bereits gebucht oder bereits storiniert");
        }

        reservierung.setReservierungsStatus(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT);

        CommandResponse reservierungCreateResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.UPDATE, CommandRequest.RequestEntityType.RESERVIERUNG, reservierung));
        if(reservierungCreateResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungCreateResponse.getMessage());
        }

        return (ReservierungDTO) reservierungCreateResponse.getEntity();
    }

    @Transactional
    public ReservierungDTO cancelReservierung(Long id) {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse reservierungResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO(id, null, null, null, null, null)));
        if (reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && reservierungResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Reservierung nicht gefunden");
        } else if(reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungResponse.getMessage());
        }

        ReservierungDTO reservierung = (ReservierungDTO) reservierungResponse.getEntity();
        if(reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.GEBUCHT || reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.STORNIERT) {
            throw new RuntimeException("Reservierung bereits gebucht oder bereits storiniert");
        }

        reservierung.setReservierungsStatus(ReservierungDTO.ReservierungsStatusDTO.STORNIERT);

        CommandResponse reservierungCreateResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.UPDATE, CommandRequest.RequestEntityType.RESERVIERUNG, reservierung));
        if(reservierungCreateResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungCreateResponse.getMessage());
        }

        return (ReservierungDTO) reservierungCreateResponse.getEntity();
    }

    @Transactional
    public List<ReservierungDTO> getReservierungen() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals(CommandResponse.ResponseEntityType.EMPTY)) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO()));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<ReservierungDTO>) response.getEntity();
    }
}
