package de.fhdw.Kino.App.producer;

import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandProducer {

    private final RabbitTemplate rabbitTemplate;

    public CommandResponse sendCommandRequest(CommandRequest request) {
        CommandResponse response = (CommandResponse) rabbitTemplate.convertSendAndReceive(
                "command.fanout.exchange",
                "",
                request
        );

        if (response == null) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Keine Antwort erhalten", CommandResponse.ResponseEntityType.EMPTY, null);
        }

        log.info("Antwort erhalten: {}", response);
        log.info("Deserialisiertes Entity: {}", response.getEntity());
        return new CommandResponse(response.getStatus(), response.getMessage(), response.getResponseEntityType(), response.getEntity());

    }

}