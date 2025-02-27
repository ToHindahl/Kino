package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuffuehrungService {

    private final CommandProducer commandProducer;

    public AuffuehrungDTO createAuffuehrung(AuffuehrungDTO dto){


        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CREATE_AUFFUEHRUNG, "auffuehrung", dto));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (AuffuehrungDTO) response.getEntity();
    }

    public List<AuffuehrungDTO> getAllAuffuehrungen() {

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_AUFFUEHRUNGEN, "", null));

        if(response.getStatus().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.getMessage());
        }

        return (List<AuffuehrungDTO>) response.getEntity();
    }
}
