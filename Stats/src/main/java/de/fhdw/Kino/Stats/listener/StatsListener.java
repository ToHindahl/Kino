package de.fhdw.Kino.Stats.listener;

import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import de.fhdw.Kino.Stats.config.RabbitMQConfig;
import de.fhdw.Kino.Stats.service.KinoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
        if(response.getStatus().equals(CommandResponse.CommandStatus.SUCCESS)) {
            switch (request.getOperation()) {
                case CREATE -> {
                    switch (response.getResponseEntityType()) {
                        case KINO -> kinoService.createKino((KinoDTO) response.getEntity());
                        case AUFFUEHRUNG -> kinoService.addAuffuehrung((AuffuehrungDTO) response.getEntity());
                        case FILM -> kinoService.addFilm((FilmDTO) response.getEntity());
                        case RESERVIERUNG -> {
                            ReservierungDTO reservierungDTO = (ReservierungDTO) response.getEntity();
                            if (reservierungDTO != null && reservierungDTO.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT)) {
                                kinoService.addReservierung(reservierungDTO);
                            }
                        }
                    }
                }
                case UPDATE -> {
                    if(response.getResponseEntityType().equals(CommandResponse.ResponseEntityType.RESERVIERUNG)) {
                        ReservierungDTO reservierungDTO = (ReservierungDTO) response.getEntity();
                        if (reservierungDTO != null && reservierungDTO.getReservierungsStatus().equals(ReservierungDTO.ReservierungsStatusDTO.GEBUCHT)) {
                            kinoService.addReservierung(reservierungDTO);
                        }
                    }
                }
                case DELETE -> {
                    switch (request.getRequestEntityType()) {
                        case FILM -> kinoService.deleteFilm(((FilmDTO) request.getEntity()).getFilmId());
                        case AUFFUEHRUNG -> kinoService.deleteAuffuehrung(((AuffuehrungDTO) request.getEntity()).getAuffuehrungId());
                        case KINO -> kinoService.reset();
                    }
                }
            }
        }
    }
}