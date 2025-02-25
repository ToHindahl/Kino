package de.fhdw.Kino.App.producer;

import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import de.fhdw.Kino.Lib.dto.GetAllFilmRequestDTO;
import de.fhdw.Kino.Lib.dto.GetAllFilmResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmProducer {

    private final RabbitTemplate rabbitTemplate;

    public CreationResponseDTO createFilm(FilmDTO dto) {
        return (CreationResponseDTO) rabbitTemplate.convertSendAndReceive("film.create.exchange","film.create.routingkey", dto);
    }

    public GetAllFilmResponseDTO getAllFilme() {
        return (GetAllFilmResponseDTO) rabbitTemplate.convertSendAndReceive("film.get.exchange","film.get.routingkey", new GetAllFilmRequestDTO());
    }
}
