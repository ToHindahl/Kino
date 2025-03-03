package de.fhdw.Kino.DB.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.DB.config.RabbitMQConfig;
import de.fhdw.Kino.DB.service.*;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private KinoService kinoService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private AuffuehrungService auffuehrungService;

    @Autowired
    private ReservierungService reservierungService;


    @Transactional
    @RabbitListener(queues = RabbitMQConfig.COMMAND_DB_QUEUE)
    public void handleCommandRequest(CommandRequest request, Message message) {

        CommandResponse response;
        try {
            // Verarbeite die Nachricht
            response = processCommandRequest(request);
        } catch (Exception e) {
            log.error("Fehler beim Verarbeiten der Nachricht: " + e.getMessage(), e);
            response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Fehler bei der Verarbeitung: " + e.getMessage(), "",  null);
        }

        // Sende Response zurück an die App
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (replyTo != null && correlationId != null) {
            rabbitTemplate.convertAndSend(replyTo, response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId); // ✅ Korrelation beibehalten
                return m;
            });
            // Sende die Response auch an den Response-Exchange für Stats
            rabbitTemplate.convertAndSend(RabbitMQConfig.RESPONSE_FANOUT_EXCHANGE, "", response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId); // ✅ Korrelation beibehalten
                return m;
            });
        }
    }

    private CommandResponse processCommandRequest(CommandRequest request) {
        log.info("DB-Modul: CRUD-Request empfangen - " + request.toString());

        CommandResponse response;

        try {
            Object entity = request.getEntity();
            if (entity instanceof LinkedHashMap) {
                // Deserialisiere das entity-Feld basierend auf dem Typnamen
                entity = deserializeEntity((LinkedHashMap<?, ?>) entity, request.getEntityType());
            }

            switch (request.getOperation()) {
                case CREATE -> {
                    switch (request.getEntityType()) {
                        case "AUFFUEHRUNG" -> response = auffuehrungService.handleAuffuehrungCreation((AuffuehrungDTO) entity);
                        case "FILM" -> response = filmService.handleFilmCreation((FilmDTO) entity);
                        case "KINO" -> response = kinoService.handleKinoCreation((KinoDTO) entity);
                        case "KUNDE" -> response = kundeService.handleKundeCreation((KundeDTO) entity);
                        case "RESERVIERUNG" -> response = reservierungService.handleReservierungCreation((ReservierungDTO) entity);
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für CREATE", "");
                    }
                }
                case READ -> {
                    switch (request.getEntityType()) {
                        case "AUFFUEHRUNG" -> response = auffuehrungService.handleAuffuehrungRequest(((Number) entity).longValue());
                        case "FILM" -> response = filmService.handleFilmRequest(((Number) entity).longValue());
                        case "KINO" -> response = kinoService.handleKinoRequest();
                        case "KUNDE" -> response = kundeService.handleKundeRequest(((Number) entity).longValue());
                        case "RESERVIERUNG" -> response = reservierungService.handleReservierungRequest(((Number) entity).longValue());
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für READ", "");
                    }
                }
                case READ_ALL -> {
                    switch (request.getEntityType()) {
                        case "AUFFUEHRUNG" -> response = auffuehrungService.handleAuffuehrungRequestAll();
                        case "FILM" -> response = filmService.handleFilmRequestAll();
                        case "KUNDE" -> response = kundeService.handleKundeRequestAll();
                        case "RESERVIERUNG" -> response = reservierungService.handleReservierungRequestAll();
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für READ_ALL", "");
                    }
                }
                case UPDATE -> {
                    if (request.getEntityType().equals("RESERVIERUNG")) {
                        response = reservierungService.handleReservierungUpdate((ReservierungDTO) entity);
                    } else {
                        response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "UPDATE nicht implementiert", "");
                    }
                }
                case DELETE -> {
                    switch (request.getEntityType()) {
                        case "AUFFUEHRUNG" -> response = auffuehrungService.handleAuffuehrungDeletion((AuffuehrungDTO) entity);
                        case "FILM" -> response = filmService.handleFilmDeletion((FilmDTO) entity);
                        case "KINO" -> response = kinoService.handleKinoReset();
                        case "KUNDE" -> response = kundeService.handleKundeDeletion((KundeDTO) entity);
                        case "RESERVIERUNG" -> response = reservierungService.handleReservierungDeletion((ReservierungDTO) entity);
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für DELETE", "");
                    }
                }
                default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Unbekannte Operation", "error", null);
            }

        } catch (Exception e) {
            log.error("Fehler beim Verarbeiten der Nachricht: " + e.getMessage(), e);
            response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Fehler bei der Verarbeitung: " + e.getMessage(), "error", null);
        }

        return response;
    }

    private Object deserializeEntity(LinkedHashMap<?, ?> entityMap, String entityType) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        try {
            switch (entityType) {
                case "RESERVIERUNG" -> {
                    return objectMapper.convertValue(entityMap, ReservierungDTO.class);
                }
                case "KINO" -> {
                    return objectMapper.convertValue(entityMap, KinoDTO.class);
                }
                case "KUNDE" -> {
                    return objectMapper.convertValue(entityMap, KundeDTO.class);
                }
                case "FILM" -> {
                    return objectMapper.convertValue(entityMap, FilmDTO.class);
                }
                case "AUFFUEHRUNG" -> {
                    return objectMapper.convertValue(entityMap, AuffuehrungDTO.class);
                }
                case "BEGINN" -> {
                    return objectMapper.convertValue(entityMap, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                }
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }
}