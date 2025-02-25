package de.fhdw.Kino.App.producers;

import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.GetKinoRequestDTO;
import de.fhdw.Kino.Lib.dto.GetKinoResponseDTO;
import de.fhdw.Kino.Lib.dto.KinoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KinoProducer {

    private final RabbitTemplate rabbitTemplate;

    public CreationResponseDTO createKino(KinoDTO dto) {
        return (CreationResponseDTO) rabbitTemplate.convertSendAndReceive("kino.create.exchange","kino.create.routingkey", dto);
    }

    public GetKinoResponseDTO getKino() {
        return (GetKinoResponseDTO) rabbitTemplate.convertSendAndReceive("kino.get.exchange","kino.get.routingkey", new GetKinoRequestDTO());
    }
}
