package de.fhdw.Kino.Stats.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.command.EntityType;
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
            requestMap.put(correlationId, request);
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
                processRequestResponsePair(request, response);
                requestMap.remove(correlationId);
            } else {
                log.warn("Kein Request für correlationId {} gefunden.", correlationId);
            }
        }
    }

    private void processRequestResponsePair(CommandRequest request, CommandResponse response) {
         try {
            if(response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS)) {
                switch (request.getOperation()) {
                    case CREATE -> {
                        switch (response.getResponseEntityType()) {
                            case KINO -> kinoService.createKino((KinoDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType()));
                            case AUFFUEHRUNG -> kinoService.addAuffuehrung((AuffuehrungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType()));
                            case FILM -> kinoService.addFilm((FilmDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType()));
                            case RESERVIERUNG -> {
                                ReservierungDTO reservierungDTO = (ReservierungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType());
                                if(reservierungDTO.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT)) {
                                    kinoService.addReservierung(reservierungDTO);
                                }
                            }
                        }
                    }
                    case UPDATE -> {
                        if(response.getResponseEntityType().equals(CommandResponse.ResponseEntityType.RESERVIERUNG)) {
                            ReservierungDTO reservierungDTO = (ReservierungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType());
                            if(reservierungDTO.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT)) {
                                kinoService.addReservierung(reservierungDTO);
                            }
                        }
                    }
                    case DELETE -> {
                        switch (request.getRequestEntityType()) {
                            case FILM -> kinoService.deleteFilm(((FilmDTO) deserializeEntity((LinkedHashMap<?, ?>) request.getEntity(), request.getRequestEntityType())).getFilmId());
                            case AUFFUEHRUNG -> kinoService.deleteAuffuehrung(((AuffuehrungDTO) deserializeEntity((LinkedHashMap<?, ?>) response.getEntity(), response.getResponseEntityType())).getAuffuehrungId());
                            case KINO -> kinoService.reset();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }

    private DTO deserializeEntity(LinkedHashMap<?, ?> entityMap, EntityType entityType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            if(entityType instanceof CommandRequest.RequestEntityType) {
                return switch ((CommandRequest.RequestEntityType) entityType) {
                    case FILM, AUFFUEHRUNG -> objectMapper.convertValue(entityMap, entityType.DTOclass);
                    default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
                };
            } else if (entityType instanceof CommandResponse.ResponseEntityType) {
                return switch ((CommandResponse.ResponseEntityType) entityType) {
                    case KINO, RESERVIERUNG, AUFFUEHRUNG, FILM, KUNDE -> objectMapper.convertValue(entityMap, entityType.DTOclass);
                    default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
                };
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
        return null;
    }
}


