package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Auffuehrung;
import de.fhdw.Kino.DB.domain.Film;
import de.fhdw.Kino.DB.domain.Kinosaal;
import de.fhdw.Kino.DB.repositories.AuffuehrungRepository;
import de.fhdw.Kino.DB.repositories.FilmRepository;
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
public class AuffuehrungService {

    private final AuffuehrungRepository auffuehrungRepository;
    private final FilmRepository filmRepository;
    private final KinoRepository kinoRepository;

    @Transactional
    public CommandResponse handleAuffuehrungCreation(AuffuehrungDTO dto) {
        Auffuehrung auffuehrung = new Auffuehrung();
        Optional<Film> film = filmRepository.findById(dto.getFilmId());
        if(film.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Film nicht gefunden", "error");
        }

        auffuehrung.setFilm(film.get());

        List<Kinosaal> alleKinosaele = kinoRepository.findAll().get(0).getKinosaele();
        if(!alleKinosaele.stream().map(Kinosaal::getKinosaalId).toList().contains(dto.getKinosaalId())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kinosaal existiert nicht", "error");
        }

        auffuehrung.setKinosaal(alleKinosaele.stream().filter(kinosaal -> kinosaal.getKinosaalId().equals(dto.getKinosaalId())).findFirst().get());

        auffuehrung.setStartzeit(dto.getStartzeit());
        auffuehrungRepository.save(auffuehrung);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"created", "auffuehrung", auffuehrung.toDTO());
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "auffuehrungsListe", auffuehrungRepository.findAll().stream().map(Auffuehrung::toDTO).toList());
    }
}
