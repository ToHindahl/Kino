package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuffuehrungService {

    private final CommandProducer commandProducer;

    @Transactional
    public AuffuehrungDTO createAuffuehrung(AuffuehrungDTO dto){

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals("null")) {
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

        CommandResponse filmResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.FILM, new FilmDTO(dto.getFilmId(), null, null)));
        if(filmResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && filmResponse.getResponseEntityType().equals("null")) {
            throw new RuntimeException("Film nicht gefunden");
        } else if(filmResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(filmResponse.getMessage());
        }

        CommandResponse auffuehrungenResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.AUFFUEHRUNG, new AuffuehrungDTO()));
        if(auffuehrungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungenResponse.getMessage());
        }

        List<AuffuehrungDTO> auffuehrungen = (List<AuffuehrungDTO>) auffuehrungenResponse.getEntity();
        if (auffuehrungen.stream().anyMatch(a -> a.getKinosaalId().equals(dto.getKinosaalId()) && a.getStartzeit().isBefore(dto.getEndzeit()) && a.getEndzeit().isAfter(dto.getStartzeit()))) {
            throw new RuntimeException("Aufführung überschneidet sich mit einer anderen Aufführung");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.CREATE, CommandRequest.RequestEntityType.AUFFUEHRUNG, dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (AuffuehrungDTO) response.getEntity();
    }

    @Transactional
    public List<AuffuehrungDTO> getAllAuffuehrungen() {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.AUFFUEHRUNG, new AuffuehrungDTO()));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<AuffuehrungDTO>) response.getEntity();
    }

    @Transactional
    public void deleteAuffuehrung(Long id) {

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.KINO, new KinoDTO()));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getResponseEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse auffuehrungResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.AUFFUEHRUNG, new AuffuehrungDTO(id, null, null, null, null, null)));
        if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && auffuehrungResponse.getResponseEntityType().equals("null")) {
            throw new RuntimeException("Aufführung nicht gefunden");
        } else if(auffuehrungResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(auffuehrungResponse.getMessage());
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.READ, CommandRequest.RequestEntityType.RESERVIERUNG, new ReservierungDTO()));
        if(reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        if(reservierungen.stream().anyMatch(r -> r.getAuffuehrungId().equals(id))) {
            throw new RuntimeException("Aufführung kann nicht gelöscht werden, da es noch Reservierungen gibt");
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.Operation.DELETE, CommandRequest.RequestEntityType.AUFFUEHRUNG, auffuehrungResponse.getEntity()));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
