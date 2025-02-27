package de.fhdw.Kino.Stats.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.Lib.dto.*;
import de.fhdw.Kino.Stats.config.RabbitMQConfig;
import de.fhdw.Kino.Stats.service.KinoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsListener {

    private final ConcurrentHashMap<String, CommandRequest> requestMap = new ConcurrentHashMap<>();

    private final KinoService kinoService;

    @RabbitListener(queues = RabbitMQConfig.COMMAND_STATS_QUEUE)
    public void handleCommandRequest(CommandRequest request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
            requestMap.put(correlationId, request); // Speichere den Request
            log.info("Stats empfängt Request mit correlationId {}: {}", correlationId, request);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_STATS_QUEUE)
    public void handleCommandResponse(CommandResponse response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
            CommandRequest request = requestMap.get(correlationId);
            if (request != null) {
                log.info("Stats empfängt Response mit correlationId {}: {}", correlationId, response);
                processRequestResponsePair(request, response); // Verarbeite Request und Response
                requestMap.remove(correlationId); // Entferne den Request aus der Map
            } else {
                log.warn("Kein Request für correlationId {} gefunden.", correlationId);
            }
        }
    }

    private void processRequestResponsePair(CommandRequest request, CommandResponse response) {
        // Hier kannst du Request und Response verarbeiten
        log.info("Verarbeite Request und Response:");
        log.info("Request: {}", request);
        log.info("Response: {}", response);

        // Beispiel: Zähle erfolgreiche und fehlgeschlagene Anfragen
        if (response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS)) {
            log.info("Anfrage war erfolgreich.");
        } else {
            log.info("Anfrage ist fehlgeschlagen.");
        }

        try {

            if(response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS)) {
                switch (request.getOperation()) {
                    case CREATE_KINO -> kinoService.createKino((KinoDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case CREATE_FILM -> kinoService.addFilm((FilmDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case CREATE_AUFFUEHRUNG -> kinoService.addAuffuehrung((AuffuehrungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case CREATE_RESERVIERUNG -> {
                        ReservierungDTO reservierungDTO = (ReservierungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType());
                        if(reservierungDTO.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.BOOKED)) {
                            kinoService.addReservierung(reservierungDTO);
                        }
                    }
                    case BOOK_RESERVIERUNG -> kinoService.addReservierung((ReservierungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case DELETE_AUFFUEHRUNG -> kinoService.deleteAuffuehrung((Long) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case DELETE_FILM -> kinoService.deleteFilm((Long) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getEntityType()));
                    case RESET -> kinoService.reset();
                }
            }
        } catch (Exception e) {
            log.error("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }

    private Object deserializeEntity(LinkedHashMap<?, ?> entityMap, String entityType) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

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
                case "id" -> {
                    return objectMapper.convertValue(entityMap, Long.class);
                }
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }
}


