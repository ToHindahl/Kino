package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Auffuehrung;
import de.fhdw.Kino.DB.model.Film;
import de.fhdw.Kino.DB.model.Kinosaal;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.FilmRepository;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuffuehrungService {

    private final AuffuehrungRepository auffuehrungRepository;

    private final FilmRepository filmRepository;

    private final KinoRepository kinoRepository;

    @Transactional
    public CommandResponse handleAuffuehrungCreation(AuffuehrungDTO dto) {

        Auffuehrung auffuehrung = new Auffuehrung();
        Film film = filmRepository.findById(dto.getFilmId()).get();
        auffuehrung.setFilm(film);

        List<Kinosaal> alleKinosaele = kinoRepository.findAll().get(0).getKinosaele();
        auffuehrung.setKinosaal(alleKinosaele.stream().filter(kinosaal -> kinosaal.getKinosaalId().equals(dto.getKinosaalId())).findFirst().get());

        auffuehrung.setStartzeit(dto.getStartzeit());
        auffuehrung.setEndzeit(dto.getEndzeit());

        auffuehrungRepository.save(auffuehrung);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"created", "auffuehrung", auffuehrung.toDTO());
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequest(Long id) {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "auffuehrung", auffuehrungRepository.findById(id).get().toDTO());
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "auffuehrungsListe", auffuehrungRepository.findAll().stream().map(Auffuehrung::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleAuffuehrungDeletion(AuffuehrungDTO dto) {
        auffuehrungRepository.deleteById(dto.getAuffuehrungId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", "null");
    }
}
