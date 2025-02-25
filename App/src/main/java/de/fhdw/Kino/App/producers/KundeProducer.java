package de.fhdw.Kino.App.producers;

import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KundeProducer {

    private final RabbitTemplate rabbitTemplate;

    public CreationResponseDTO createKunde(KundeDTO dto) {
        return (CreationResponseDTO) rabbitTemplate.convertSendAndReceive("kunde.create.exchange","kunde.create.routingkey", dto);
    }
}
