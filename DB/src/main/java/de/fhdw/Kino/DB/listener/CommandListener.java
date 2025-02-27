package de.fhdw.Kino.DB.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.DB.config.RabbitMQConfig;
import de.fhdw.Kino.DB.service.*;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

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
            response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Fehler bei der Verarbeitung: " + e.getMessage(), "error",  null);
        }

        log.info("Antwort erstellt: " + response);
        // Sende die Antwort zurück
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (replyTo != null && correlationId != null) {
            rabbitTemplate.convertAndSend(replyTo, response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId);
                return m;
            });
        } else {
            log.error("Keine replyTo-Queue oder correlationId angegeben!");
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
                case CREATE_RESERVIERUNG -> response = reservierungService.handleReservierungCreation((ReservierungDTO) entity);
                case CREATE_KINO -> response = kinoService.handleKinoCreation((KinoDTO) entity);
                case CREATE_KUNDE -> response = kundeService.handleKundeCreation((KundeDTO) entity);
                case CREATE_FILM -> response = filmService.handleFilmCreation((FilmDTO) entity);
                case CREATE_AUFFUEHRUNG -> response = auffuehrungService.handleAuffuehrungCreation((AuffuehrungDTO) entity);
                case GET_AUFFUEHRUNGEN -> response = auffuehrungService.handleAuffuehrungRequestAll();
                case GET_KINO -> response = kinoService.handleKinoRequest();
                case GET_FILME -> response = filmService.handleFilmRequestAll();
                case GET_KUNDEN -> response = kundeService.handleKundeRequestAll();
                case GET_RESERVIERUNGEN -> response = reservierungService.handleReservierungRequestAll();
                case CANCEL_RESERVIERUNG -> response = reservierungService.handleReservierungCancelation(((Number) entity).longValue());
                case BOOK_RESERVIERUNG -> response = reservierungService.handleReservierungBooking(((Number) entity).longValue());
                default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "unbekanntes Objekt erhalten", "error", null);
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
                default -> throw new IllegalArgumentException("Unbekannter Entity-Typ: " + entityType);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fehler beim Deserialisieren des entity-Felds: " + e.getMessage(), e);
        }
    }
}