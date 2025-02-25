package de.fhdw.Kino.DB.listener;

import com.rabbitmq.client.Channel;
import de.fhdw.Kino.DB.config.RabbitMQConfig;
import de.fhdw.Kino.DB.domain.Film;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrudListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.CRUD_DB_QUEUE)
    public void handleCrudRequest(CrudRequest request, Message message) {
        // Verarbeite die Nachricht (z. B. speichere in der DB)
        CrudResponse response = processCrudRequest(request);

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

    private CrudResponse processCrudRequest(CrudRequest request) {
        log.info("Stats-Modul: CRUD-Request empfangen - " + request.toString());

        CrudResponse response;
        switch (request.entity().getClass().getSimpleName()) {
            case "KundeDTO":
                response = handleKundeRequest((KundeDTO) request.entity());
                break;
            case "FilmDTO":
                response = handleFilmRequest((FilmDTO) request.entity());
                break;
            default:
                response = new CrudResponse(-1L, CrudResponse.Status.ERROR, "Unbekannte Entität: " + request.entity().getClass().getSimpleName());
        }
        return response;
    }

    private CrudResponse handleKundeRequest(KundeDTO kundeDTO) {
        // Logik für Kunde
        return new CrudResponse(1L, CrudResponse.Status.SUCCESS, "Kunde erfolgreich verarbeitet");
    }

    private CrudResponse handleFilmRequest(FilmDTO filmDTO) {
        // Logik für Film
        return new CrudResponse(1L, CrudResponse.Status.SUCCESS, "Film erfolgreich verarbeitet");
    }
}