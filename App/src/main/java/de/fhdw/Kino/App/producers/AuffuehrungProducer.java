package de.fhdw.Kino.App.producers;

import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.GetAllAuffuehrungRequestDTO;
import de.fhdw.Kino.Lib.dto.GetAllAuffuehrungResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuffuehrungProducer {

    private final RabbitTemplate rabbitTemplate;

    public CreationResponseDTO createAuffuehrung(AuffuehrungDTO dto) {
        return (CreationResponseDTO) rabbitTemplate.convertSendAndReceive("auffuehrung.create.exchange","auffuehrung.create.routingkey", dto);
    }

    public GetAllAuffuehrungResponseDTO getAllAuffuehrungen() {
        return (GetAllAuffuehrungResponseDTO) rabbitTemplate.convertSendAndReceive("auffuehrung.get_all.exchange","auffuehrung.get_all.routingkey", new GetAllAuffuehrungRequestDTO());
    }

}
