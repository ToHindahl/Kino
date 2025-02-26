package de.fhdw.Kino.App.producer;

import de.fhdw.Kino.Lib.dto.CommandRequest;
import de.fhdw.Kino.Lib.dto.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandProducer {

    private final RabbitTemplate rabbitTemplate;

    public CommandResponse sendCommandRequest(CommandRequest request) {
        // Sende die Nachricht und warte auf eine Antwort
        CommandResponse response = (CommandResponse) rabbitTemplate.convertSendAndReceive(
                "command.fanout.exchange", // Exchange
                "", // Routing Key (leer für Fanout)
                request // Nachricht
        );

        return response != null ? response : new CommandResponse(-1L, CommandResponse.CommandStatus.ERROR, "Keine Antwort erhalten");
    }
}