package de.fhdw.Kino.App.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    public CommandResponse sendCommandRequest(CommandRequest request) {

        // Sende die Nachricht und warte auf eine Antwort
        CommandResponse response = (CommandResponse) rabbitTemplate.convertSendAndReceive(
                "command.fanout.exchange", // Exchange
                "", // Routing Key (leer für Fanout)
                request // Nachricht
        );

        // Deserialisiere die Antwort

        Object entity;
        try {
            assert response != null;
            log.info("Antwort erhalten: " + response);
            entity = response.getEntity();
            if (entity instanceof LinkedHashMap) {
                // Deserialisiere das entity-Feld basierend auf dem Typnamen
                return new CommandResponse(response.getStatus(), response.getMessage(), response.getEntityType(), deserializeEntity((LinkedHashMap<?, ?>) entity, response.getEntityType()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }


        return response != null ? new CommandResponse(response.getStatus(), response.getMessage(), response.getEntityType(), entity) : new CommandResponse(CommandResponse.CommandStatus.ERROR, "Keine Antwort erhalten", "error", null);
    }

    private Object deserializeEntity(LinkedHashMap<?, ?> entityMap, String entityType) {
        if (entityType == null) {
            throw new IllegalArgumentException("Entity-Typ darf nicht null sein");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            switch (entityType) {
                case "reservierung" -> {
                    return objectMapper.convertValue(entityMap, ReservierungDTO.class);
                }
                case "reservierungsListe" -> {
                    return objectMapper.convertValue(entityMap, objectMapper.getTypeFactory().constructCollectionType(List.class, ReservierungDTO.class));
                }
                case "kino" -> {
                    return objectMapper.convertValue(entityMap, KinoDTO.class);
                }
                case "kunde" -> {
                    return objectMapper.convertValue(entityMap, KundeDTO.class);
                }
                case "kundenListe" -> {
                    return objectMapper.convertValue(entityMap, objectMapper.getTypeFactory().constructCollectionType(List.class, KundeDTO.class));
                }
                case "film" -> {
                    return objectMapper.convertValue(entityMap, FilmDTO.class);
                }
                case "filmListe" -> {
                    return objectMapper.convertValue(entityMap, objectMapper.getTypeFactory().constructCollectionType(List.class, FilmDTO.class));
                }
                case "auffuehrung" -> {
                    return objectMapper.convertValue(entityMap, AuffuehrungDTO.class);
                }
                case "auffuehrungsListe" -> {
                    return objectMapper.convertValue(entityMap, objectMapper.getTypeFactory().constructCollectionType(List.class, AuffuehrungDTO.class));
                }
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }


}