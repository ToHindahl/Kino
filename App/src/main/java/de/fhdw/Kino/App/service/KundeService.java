package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KundeService {

    private final CommandProducer commandProducer;

    public KundeDTO createKunde(KundeDTO dto){

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.CREATE, "KUNDE", dto));
        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (KundeDTO) response.getEntity();
    }

    public List<KundeDTO> getAllKunden() {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KUNDENLISTE", ""));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<KundeDTO>) response.getEntity();
    }

    public void deleteKunde(Long id) {

        UUID transactionId = UUID.randomUUID();

        CommandResponse kinoResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KINO", null));
        if (kinoResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kinoResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kino noch nicht initialisiert");
        } else if(kinoResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kinoResponse.getMessage());
        }

        CommandResponse kundeResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "KUNDE", id));
        if (kundeResponse.getStatus().equals(CommandResponse.CommandStatus.SUCCESS) && kundeResponse.getEntityType().equals("null")) {
            throw new RuntimeException("Kunde nicht gefunden");
        } else if(kundeResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(kundeResponse.getMessage());
        }

        CommandResponse reservierungenResponse = commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.READ, "RESERVIERUNGSLISTE", null));
        if (reservierungenResponse.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(reservierungenResponse.getMessage());
        }

        List<ReservierungDTO> reservierungen = (List<ReservierungDTO>) reservierungenResponse.getEntity();
        if(reservierungen.stream().anyMatch(r -> r.getKundeId().equals(id))) {
            throw new RuntimeException("Kunde hat noch Reservierungen");
        }

        CommandResponse response =  commandProducer.sendCommandRequest(new CommandRequest(transactionId, CommandRequest.Operation.DELETE, "KUNDE", kundeResponse.getEntity()));
        if (response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }
    }
}
