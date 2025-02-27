package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuffuehrungService {

    private final CommandProducer commandProducer;

    public AuffuehrungDTO createAuffuehrung(AuffuehrungDTO dto){

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CREATE_AUFFUEHRUNG, "auffuehrung", dto));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (AuffuehrungDTO) response.entity();
    }

    public List<AuffuehrungDTO> getAllAuffuehrungen() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_AUFFUEHRUNGEN, "", null));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (List<AuffuehrungDTO>) response.entity();
    }
}
