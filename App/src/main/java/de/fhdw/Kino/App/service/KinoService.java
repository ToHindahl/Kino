package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KinoService {

    private final CommandProducer commandProducer;

    public KinoDTO createKino(KinoDTO dto){

        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.CREATE_KINO, "kino", dto));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return (KinoDTO) response.entity();
    }

    public KinoDTO getKino() {


        CommandResponse response = commandProducer.sendCommandRequest(new CommandRequest(CommandRequest.CommandType.GET_KINO, "", null));

        if(response.status().equals(CommandResponse.CommandStatus.ERROR)) {
            throw new RuntimeException(response.message());
        }
        return (KinoDTO) response.entity();
    }
}
