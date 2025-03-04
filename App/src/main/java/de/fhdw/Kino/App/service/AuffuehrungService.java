package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuffuehrungService {

    private final CommandProducer commandProducer;

    public AuffuehrungDTO createAuffuehrung(AuffuehrungDTO dto){

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        if(dto.getStartzeit().isAfter(dto.getEndzeit())) {
            throw new RuntimeException("Startzeit muss vor Endzeit liegen");
        }

        if(dto.getStartzeit().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Aufführung muss in der Zukunft liegen");
        }

        CommandResponse filmResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "FILM", dto.getFilmId()));
        if(filmResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && filmResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Film nicht gefunden");
        } else if(filmResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmResponse.getMessage());
        }

        CommandResponse auffuehrungenResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "AUFFUEHRUNGSLISTE", null));
        if(auffuehrungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungenResponse.getMessage());
        }

        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) auffuehrungenResponse.getEntity();
        if (auffuehrungen.stream().anyMatch(a -> a.getKinosaalId().equals(dto.getKinosaalId()) && a.getStartzeit().isBefore(dto.getEndzeit()) && a.getEndzeit().isAfter(dto.getStartzeit()))) {
            throw new RuntimeException("Aufführung überschneidet sich mit einer anderen Aufführung");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.CREATE, "AUFFUEHRUNG", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (AuffuehrungDTO) response.getEntity();
    }

    public List<AuffuehrungDTO> getAllAuffuehrungen() {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "AUFFUEHRUNGLISTE", null));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<AuffuehrungDTO>) response.getEntity();
    }

    public void deleteAuffuehrung(Long id) {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse auffuehrungResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "AUFFUEHRUNG", id));
        if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && auffuehrungResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Aufführung nicht gefunden");
        } else if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungResponse.getMessage());
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "RESERVIERUNGSLISTE", null));
        if(reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        if(reservierungen.stream().anyMatch(r -> r.getAuffuehrungId().equals(id))) {
            throw new RuntimeException("Aufführung kann nicht gelöscht werden, da es noch Reservierungen gibt");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "AUFFUEHRUNG", auffuehrungResponse));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
