package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservierungService {

    private final CommandProducer commandProducer;

    public ReservierungDTO createReservierung(ReservierungDTO dto) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CREATE_RESERVIERUNG, "reservierung", dto));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (ReservierungDTO) response.getEntity();
    }

    public ReservierungDTO bookReservierung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.BOOK_RESERVIERUNG, "reservierung", id));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (ReservierungDTO) response.getEntity();
    }

    public ReservierungDTO cancelReservierung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CANCEL_RESERVIERUNG, "reservierung", id));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (ReservierungDTO) response.getEntity();
    }

    public List<ReservierungDTO> getReservierungenByAuffuehrung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_RESERVIERUNGEN, "", null));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return ((List<ReservierungDTO>) response.getEntity()).stream().filter(r -> r.getAuffuehrungId().equals(id)).toList();
    }

    public List<ReservierungDTO> getReservierungen() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_RESERVIERUNGEN, "", null));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<ReservierungDTO>) response.getEntity();
    }



}
