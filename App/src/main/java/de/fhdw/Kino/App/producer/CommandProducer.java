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

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandProducer {

    private final RabbitTemplate rabbitTemplate;

    // In CommandProducer.java

    public CommandResponse sendCommandRequest(CommandRequest request) {
        CommandResponse response = (CommandResponse) rabbitTemplate.convertSendAndReceive(
                "command.fanout.exchange",
                "",
                request
        );

        if (response == null) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Keine Antwort erhalten", "error", null);
        }

        try {
            Object entity = response.getEntity();
            String entityType = response.getEntityType();
            Object deserializedEntity = deserializeEntity(entity, entityType);
            log.info("Antwort erhalten: {}", response);
            log.info("Deserialisiertes Entity: {}", deserializedEntity);
            return new CommandResponse(response.getStatus(), response.getMessage(), response.getEntityType(), deserializedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }

    private Object deserializeEntity(Object entity, String entityType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            return switch (entityType) {
                case "reservierung" -> objectMapper.convertValue(entity, ReservierungDTO.class);
                case "reservierungsListe" -> objectMapper.convertValue(entity,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ReservierungDTO.class));
                case "kino" -> objectMapper.convertValue(entity, KinoDTO.class);
                case "kunde" -> objectMapper.convertValue(entity, KundeDTO.class);
                case "kundenListe" -> objectMapper.convertValue(entity,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, KundeDTO.class));
                case "film" -> objectMapper.convertValue(entity, FilmDTO.class);
                case "filmListe" -> objectMapper.convertValue(entity,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, FilmDTO.class));
                case "auffuehrung" -> objectMapper.convertValue(entity, AuffuehrungDTO.class);
                case "auffuehrungsListe" -> objectMapper.convertValue(entity,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, AuffuehrungDTO.class));
                case "null" -> null;
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            };
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }

}