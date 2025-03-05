package de.fhdw.Kino.DB.listener;

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
            response = processCommandRequest(request);
        } catch (Exception e) {
            log.error("Fehler beim Verarbeiten der Nachricht: {}", e.getMessage(), e);
            response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Fehler bei der Verarbeitung: " + e.getMessage(), CommandResponse.ResponseEntityType.EMPTY);
        }

        // Rücksendung der Response an App
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (replyTo != null && correlationId != null) {
            rabbitTemplate.convertAndSend(replyTo, response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId);
                return m;
            });
            // Rücksendung der Response an Fanout
            rabbitTemplate.convertAndSend(RabbitMQConfig.RESPONSE_FANOUT_EXCHANGE, "", response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId);
                return m;
            });
        }
    }

    private CommandResponse processCommandRequest(CommandRequest request) {

        log.info("DB-Modul: CRUD-Request empfangen - {}", request.toString());

        CommandResponse response;
        try {
            Object entity = request.getEntity();

            switch (request.getOperation()) {
                case CREATE -> {
                    switch (request.getRequestEntityType()) {
                        case AUFFUEHRUNG -> response = auffuehrungService.handleAuffuehrungCreation((AuffuehrungDTO) entity);
                        case FILM -> response = filmService.handleFilmCreation((FilmDTO) entity);
                        case KINO -> response = kinoService.handleKinoCreation((KinoDTO) entity);
                        case KUNDE -> response = kundeService.handleKundeCreation((KundeDTO) entity);
                        case RESERVIERUNG -> response = reservierungService.handleReservierungCreation((ReservierungDTO) entity);
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für CREATE", CommandResponse.ResponseEntityType.EMPTY);
                    }
                }
                case READ -> {
                    switch (request.getRequestEntityType()) {
                        case AUFFUEHRUNG -> {
                            AuffuehrungDTO dto = (AuffuehrungDTO) request.getEntity();
                            if(dto.getAuffuehrungId() != null) {
                                response = auffuehrungService.handleAuffuehrungRequest(dto.getAuffuehrungId());
                            } else {
                                response = auffuehrungService.handleAuffuehrungRequestAll();
                            }
                        }
                        case FILM -> {
                            FilmDTO dto = (FilmDTO) request.getEntity();
                            if(dto.getFilmId() != null) {
                                response = filmService.handleFilmRequest(dto.getFilmId());
                            } else {
                                response = filmService.handleFilmRequestAll();
                            }
                        }
                        case KINO -> response = kinoService.handleKinoRequest();
                        case KUNDE -> {
                            KundeDTO dto = (KundeDTO) request.getEntity();
                            if(dto.getKundeId() != null) {
                                response = kundeService.handleKundeRequest(dto.getKundeId());
                            } else {
                                response = kundeService.handleKundeRequestAll();
                            }
                        }
                        case RESERVIERUNG -> {
                            ReservierungDTO dto = (ReservierungDTO) request.getEntity();
                            if(dto.getReservierungId() != null) {
                                response = reservierungService.handleReservierungRequest(dto.getReservierungId());
                            } else {
                                response = reservierungService.handleReservierungRequestAll();
                            }
                        }
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für READ", CommandResponse.ResponseEntityType.EMPTY);
                    }
                }
                case UPDATE -> {
                    if (request.getRequestEntityType().equals(CommandRequest.RequestEntityType.RESERVIERUNG)) {
                        response = reservierungService.handleReservierungUpdate((ReservierungDTO) entity);
                    } else {
                        response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "UPDATE nicht implementiert", CommandResponse.ResponseEntityType.EMPTY);
                    }
                }
                case DELETE -> {
                    switch (request.getRequestEntityType()) {
                        case AUFFUEHRUNG -> response = auffuehrungService.handleAuffuehrungDeletion((AuffuehrungDTO) entity);
                        case FILM -> response = filmService.handleFilmDeletion((FilmDTO) entity);
                        case KINO -> response = kinoService.handleKinoReset();
                        case KUNDE -> response = kundeService.handleKundeDeletion((KundeDTO) entity);
                        case RESERVIERUNG -> response = reservierungService.handleReservierungDeletion((ReservierungDTO) entity);
                        default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Ungültiger EntityType für DELETE", CommandResponse.ResponseEntityType.EMPTY);
                    }
                }
                default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Unbekannte Operation", CommandResponse.ResponseEntityType.EMPTY);
            }
        } catch (Exception e) {
            log.error("Fehler beim Verarbeiten der Nachricht: {}", e.getMessage(), e);
            response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "Fehler bei der Verarbeitung: " + e.getMessage(), CommandResponse.ResponseEntityType.EMPTY);
        }

        return response;
    }

}