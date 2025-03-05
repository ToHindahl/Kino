package de.fhdw.Kino.App.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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

        try {
            DTO deserializedEntity = deserializeEntity(response.getEntity(), response.getResponseEntityType());
            log.info("Antwort erhalten: {}", response);
            log.info("Deserialisiertes Entity: {}", deserializedEntity);
            return new CommandResponse(response.getStatus(), response.getMessage(), response.getResponseEntityType(), deserializedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }

    private DTO deserializeEntity(Object entity, CommandResponse.ResponseEntityType responseEntityType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {

            return switch (responseEntityType) {
                case RESERVIERUNG, KINO, KUNDE, FILM, AUFFUEHRUNG -> objectMapper.convertValue(entity, responseEntityType.DTOclass);
                case RESERVIERUNGSLISTE, KUNDENLISTE, FILMLISTE, AUFFUEHRUNGSLISTE -> objectMapper.convertValue(entity, objectMapper.getTypeFactory().constructCollectionType(List.class, responseEntityType.DTOclass));
                case EMPTY -> null;
            };
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }
}