package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.domain.Film;
import de.fhdw.Kino.DB.repositories.FilmRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FilmListener {

    private final FilmRepository filmRepository;

    @Transactional
    @RabbitListener(queues = "film.create.queue")
    public CreationResponseDTO handleFilmCreation(FilmDTO dto) {
        Film film = new Film();
        film.setFilmTitel(dto.filmTitel());
        filmRepository.save(film);
        return new CreationResponseDTO(film.getFilmId(), StatusDTO.SUCCESS,"success");
    }

    @Transactional
    @RabbitListener(queues = "film.get_all.queue")
    public GetAllFilmResponseDTO handleFilmRequestAll(GetAllFilmRequestDTO dto) {
        return new GetAllFilmResponseDTO(filmRepository.findAll().stream().map(film -> new FilmDTO(film.getFilmId(), film.getFilmTitel())).toList(), StatusDTO.SUCCESS,"success");
    }

}
