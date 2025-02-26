package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.config.RabbitMQConfig;
import de.fhdw.Kino.DB.service.KinoService;
import de.fhdw.Kino.DB.service.ReservierungService;
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
    private ReservierungService reservierungService;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.COMMAND_DB_QUEUE)
    public void handleCommandRequest(CommandRequest request, Message message) {
        // Verarbeite die Nachricht (z. B. speichere in der DB)
        CommandResponse response = processCommandRequest(request);

        // Hole die replyTo-Queue und correlationId aus den AMQP-Eigenschaften
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (replyTo != null && correlationId != null) {
            // Sende die Antwort zurück mit der correlationId
            rabbitTemplate.convertAndSend(replyTo, response, m -> {
                m.getMessageProperties().setCorrelationId(correlationId);
                return m;
            });
        } else {
            log.error("Keine replyTo-Queue oder correlationId angegeben!");
        }
    }

    private CommandResponse processCommandRequest(CommandRequest request) {
        log.info("Stats-Modul: CRUD-Request empfangen - " + request.toString());

        CommandResponse response;

        switch (request.operation()) {
            case CREATE_KINO -> response = kinoService.handleKinoCreation((KinoDTO) request.entity());
            case CREATE_KUNDE -> response = handleKundeRequest((KundeDTO) request.entity());
            case CREATE_FILM -> response = handleFilmRequest((FilmDTO) request.entity());
            case CREATE_RESERVIERUNG -> response = handleFilmRequest((FilmDTO) request.entity());
            case CREATE_AUFFUEHRUNG -> response = handleFilmRequest((FilmDTO) request.entity());
            case GET_AUFFUEHRUNGEN -> response = handleFilmRequest((FilmDTO) request.entity());
            case GET_KINO -> response = handleFilmRequest((FilmDTO) request.entity());
            case GET_FILME -> response = handleFilmRequest((FilmDTO) request.entity());
            case CANCEL_RESERVIERUNG -> response = handleFilmRequest((FilmDTO) request.entity());
            case BOOK_RESERVIERUNG -> response = handleFilmRequest((FilmDTO) request.entity());
            default -> response = new CommandResponse(CommandResponse.CommandStatus.ERROR, "unbekanntes Objekt erhalten", null)
        }
        return response;
    }

    private CommandResponse handleKundeRequest(KundeDTO kundeDTO) {
        // Logik für Kunde
        return new CommandResponse(1L, CommandResponse.CommandStatus.SUCCESS, "Kunde erfolgreich verarbeitet");
    }

    private CommandResponse handleFilmRequest(FilmDTO filmDTO) {
        // Logik für Film
        return new CommandResponse(1L, CommandResponse.CommandStatus.SUCCESS, "Film erfolgreich verarbeitet");
    }
}