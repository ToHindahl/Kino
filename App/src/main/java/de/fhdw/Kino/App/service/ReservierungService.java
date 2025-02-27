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

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (ReservierungDTO) response.entity();
    }

    public ReservierungDTO bookReservierung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.BOOK_RESERVIERUNG, "reservierung", new ReservierungDTO(id, null, null, null, null)));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (ReservierungDTO) response.entity();
    }

    public ReservierungDTO cancelReservierung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CANCEL_RESERVIERUNG, "reservierung", new ReservierungDTO(id, null, null, null, null)));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (ReservierungDTO) response.entity();
    }

    public List<ReservierungDTO> getReservierungenByAuffuehrung(Long id) {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_RESERVIERUNGEN, "", null));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return ((List<ReservierungDTO>) response.entity()).stream().filter(r -> r.auffuehrungId().equals(id)).toList();
    }

    public List<ReservierungDTO> getReservierungen() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_RESERVIERUNGEN, "", null));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (List<ReservierungDTO>) response.entity();
    }



}
