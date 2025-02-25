package de.fhdw.Kino.App.producers;

import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReservierungProducer {

    private final RabbitTemplate rabbitTemplate;

    public CreationResponseDTO createReservierung(ReservierungDTO dto) {
        return (CreationResponseDTO) rabbitTemplate.convertSendAndReceive("reservierung.create.exchange","reservierung.create.routingkey", dto);
    }

    public BookReservierungResponseDTO bookReservierung(BookReservierungRequestDTO dto) {
        return (BookReservierungResponseDTO) rabbitTemplate.convertSendAndReceive("reservierung.book.exchange","reservierung.book.routingkey", dto);
    }

    public CancelReservierungResponseDTO cancelReservierung(CancelReservierungRequestDTO dto) {
        return (CancelReservierungResponseDTO) rabbitTemplate.convertSendAndReceive("reservierung.cancel.exchange","reservierung.cancel.routingkey", dto);
    }

}
