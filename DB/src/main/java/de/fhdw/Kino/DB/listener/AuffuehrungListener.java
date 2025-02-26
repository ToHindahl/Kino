package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.domain.Auffuehrung;
import de.fhdw.Kino.DB.domain.Film;
import de.fhdw.Kino.DB.domain.Kinosaal;
import de.fhdw.Kino.DB.repositories.FilmRepository;
import de.fhdw.Kino.DB.repositories.AuffuehrungRepository;
import de.fhdw.Kino.DB.repositories.KinoRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuffuehrungListener {

    private final AuffuehrungRepository auffuehrungRepository;
    private final FilmRepository filmRepository;
    private final KinoRepository kinoRepository;

    @Transactional
    @RabbitListener(queues = "auffuehrung.create.queue")
    public CreationResponseDTO handleAuffuehrungCreation(AuffuehrungDTO dto) {
        Auffuehrung auffuehrung = new Auffuehrung();
        Optional<Film> film = filmRepository.findById(dto.filmId());
        if(film.isEmpty()) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Film nicht gefunden");
        }

        auffuehrung.setFilm(film.get());

        List<Kinosaal> alleKinosaele = kinoRepository.findAll().get(0).getKinosaele();
        if(!alleKinosaele.stream().map(Kinosaal::getKinosaalId).toList().contains(dto.kinosaalId())) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Kinosaal existiert nicht");
        }

        auffuehrung.setKinosaal(alleKinosaele.stream().filter(kinosaal -> kinosaal.getKinosaalId().equals(dto.kinosaalId())).findFirst().get());

        auffuehrung.setStartzeit(dto.startzeit());
        auffuehrungRepository.save(auffuehrung);

        return new CreationResponseDTO(auffuehrung.getAuffuehrungId(), StatusDTO.SUCCESS,"success");
    }

    @Transactional
    @RabbitListener(queues = "auffuehrung.get_all.queue")
    public GetAllAuffuehrungResponseDTO handleAuffuehrungRequestAll(GetAllAuffuehrungRequestDTO dto) {
        return new GetAllAuffuehrungResponseDTO(auffuehrungRepository.findAll().stream().map(auffuehrung -> new AuffuehrungDTO(auffuehrung.getAuffuehrungId(), auffuehrung.getStartzeit(), auffuehrung.getFilm().getFilmId(), auffuehrung.getKinosaal().getKinosaalId())).toList(), StatusDTO.SUCCESS,"success");
    }
}
