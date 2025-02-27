package de.fhdw.Kino.App.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

        Object entity;
        try {
            entity = response.entity();
            if (entity instanceof LinkedHashMap) {
                // Deserialisiere das entity-Feld basierend auf dem Typnamen
                entity = deserializeEntity((LinkedHashMap<?, ?>) entity, request.entityType());
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }


        return response != null ? new CommandResponse(response.status(), response.message(), response.entityType(), entity) : new CommandResponse(CommandResponse.CommandStatus.ERROR, "Keine Antwort erhalten", "error", null);
    }

    private Object deserializeEntity(LinkedHashMap<?, ?> entityMap, String entityType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            switch (entityType) {
                case "reservierung" -> {
                    return objectMapper.convertValue(entityMap, ReservierungDTO.class);
                }
                case "kino" -> {
                    return objectMapper.convertValue(entityMap, KinoDTO.class);
                }
                case "kunde" -> {
                    return objectMapper.convertValue(entityMap, KundeDTO.class);
                }
                case "film" -> {
                    return objectMapper.convertValue(entityMap, FilmDTO.class);
                }
                case "auffuehrung" -> {
                    return objectMapper.convertValue(entityMap, AuffuehrungDTO.class);
                }
                case "reservierungsListe" -> {
                    return objectMapper.convertValue(entityMap, ArrayList.class);
                }
                case "auffuehrungsListe" -> {
                    return objectMapper.convertValue(entityMap, ArrayList.class);
                }
                case "filmListe" -> {
                    return objectMapper.convertValue(entityMap, ArrayList.class);
                }
                case "error" -> {
                    return objectMapper.convertValue(entityMap, Object.class);
                }
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }


}