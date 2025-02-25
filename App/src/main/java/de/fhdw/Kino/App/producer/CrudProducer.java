package de.fhdw.Kino.App.producer;

import de.fhdw.Kino.Lib.dto.CrudRequest;
import de.fhdw.Kino.Lib.dto.CrudResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrudProducer {

    private final RabbitTemplate rabbitTemplate;

    public CrudResponse sendCrudRequest(CrudRequest request) {
        // Sende die Nachricht und warte auf eine Antwort
        CrudResponse response = (CrudResponse) rabbitTemplate.convertSendAndReceive(
                "crud.fanout.exchange", // Exchange
                "", // Routing Key (leer für Fanout)
                request // Nachricht
        );

        return response != null ? response : new CrudResponse(-1L, CrudResponse.Status.ERROR, "Keine Antwort erhalten");
    }
}