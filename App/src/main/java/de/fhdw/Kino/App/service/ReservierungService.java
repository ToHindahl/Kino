package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservierungService {

    private final CommandProducer commandProducer;

    public ReservierungDTO createReservierung(ReservierungDTO dto) {

        if(dto.getSitzplatzIds().isEmpty()) {
            throw new RuntimeException("Keine Sitzplätze ausgewählt");
        }

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        KinoDTO kino = (KinoDTO) kinoResponse.getEntity();

        CommandResponse auffuehrungResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "AUFFUEHRUNG", dto.getAuffuehrungId()));
        if (auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && auffuehrungResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Aufführung nicht gefunden");
        } else if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungResponse.getMessage());
        }

        CommandResponse kundeResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KUNDE", dto.getKundeId()));
        if (kundeResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kundeResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kunde nicht gefunden");
        } else if(kundeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kundeResponse.getMessage());
        }

        AuffuehrungDTO auffuehrung = (AuffuehrungDTO) auffuehrungResponse.getEntity();
        if (auffuehrung.getStartzeit().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Aufführung noch nicht gestartet");
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "RESERVIERUNG", null));
        if(reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        List<SitzplatzDTO> alleSitzplaetze = new ArrayList<>();

        kino.getKinosaele().stream().filter(s -> s.getKinosaalId().equals(auffuehrung.getKinosaalId())).findFirst().get().getSitzreihen().forEach(reihe -> {
            reihe.getSitzplaetze().forEach(sitzplatz -> {
                alleSitzplaetze.add(sitzplatz);
            });
        });

        List<Long> reservierteSitzplaetze = new ArrayList<>();
        reservierungen.stream().filter(r -> r.getAuffuehrungId().equals(auffuehrung.getAuffuehrungId()) && !(r.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.STORNIERT))).forEach(r -> {
            reservierteSitzplaetze.addAll(r.getSitzplatzIds());
        });

        alleSitzplaetze.removeIf(reservierteSitzplaetze::contains);
        if(!alleSitzplaetze.stream().map(SitzplatzDTO::getSitzplatzId).toList().containsAll(dto.getSitzplatzIds())) {
            throw new RuntimeException("Sitzplatz nicht verfügbar");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.CREATE, "RESERVIERUNG", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (ReservierungDTO) response.getEntity();
    }

    public ReservierungDTO bookReservierung(Long id) {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse reservierungResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "RESERVIERUNG", id));
        if (reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && reservierungResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Reservierung nicht gefunden");
        } else if(reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungResponse.getMessage());
        }

        ReservierungDTO reservierung = (ReservierungDTO) reservierungResponse.getEntity();
        if(reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.GEBUCHT || reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.STORNIERT) {
            throw new RuntimeException("Reservierung bereits gebucht oder bereits storiniert");
        }

        reservierung.setReservierungsStatus(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT);

        CommandResponse reservierungCreateResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.UPDATE, "RESERVIERUNG", reservierung));
        if(reservierungCreateResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungCreateResponse.getMessage());
        }

        return (ReservierungDTO) reservierungCreateResponse.getEntity();
    }

    public ReservierungDTO cancelReservierung(Long id) {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse reservierungResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "RESERVIERUNG", id));
        if (reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && reservierungResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Reservierung nicht gefunden");
        } else if(reservierungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungResponse.getMessage());
        }

        ReservierungDTO reservierung = (ReservierungDTO) reservierungResponse.getEntity();
        if(reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.GEBUCHT || reservierung.getReservierungsStatus() == ReservierungDTO.ReservierungsStatusDTO.STORNIERT) {
            throw new RuntimeException("Reservierung bereits gebucht oder bereits storiniert");
        }

        reservierung.setReservierungsStatus(ReservierungDTO.ReservierungsStatusDTO.STORNIERT);

        CommandResponse reservierungCreateResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.UPDATE, "RESERVIERUNG", reservierung));
        if(reservierungCreateResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungCreateResponse.getMessage());
        }

        return (ReservierungDTO) reservierungCreateResponse.getEntity();
    }

    public List<ReservierungDTO> getReservierungen() {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ_ALL, "RESERVIERUNG", null));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<ReservierungDTO>) response.getEntity();
    }
}
