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
        Film film = filmRepository.findById(dto.getFilmId()).get();
        auffuehrung.setFilm(film);

        List<Kinosaal> alleKinosaele = kinoRepository.findAll().get(0).getKinosaele();
        auffuehrung.setKinosaal(alleKinosaele.stream().filter(kinosaal -> kinosaal.getKinosaalId().equals(dto.getKinosaalId())).findFirst().get());

        auffuehrung.setStartzeit(dto.getStartzeit());
        auffuehrung.setEndzeit(dto.getEndzeit());

        auffuehrungRepository.save(auffuehrung);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"created", CommandResponse.ResponseEntityType.AUFFUEHRUNG, auffuehrung.toDTO());
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequest(Long id) {
        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(id);
        return auffuehrung.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", CommandResponse.ResponseEntityType.AUFFUEHRUNG, value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", CommandResponse.ResponseEntityType.EMPTY));
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", CommandResponse.ResponseEntityType.AUFFUEHRUNGSLISTE, auffuehrungRepository.findAll().stream().map(Auffuehrung::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleAuffuehrungDeletion(AuffuehrungDTO dto) {

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(dto.getAuffuehrungId());
        if(auffuehrung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "not found", CommandResponse.ResponseEntityType.EMPTY);
        }

        if(!dto.getVersion().equals(auffuehrung.get().getVersion())) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "version mismatch", CommandResponse.ResponseEntityType.EMPTY);
        }

        auffuehrungRepository.deleteById(dto.getAuffuehrungId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", CommandResponse.ResponseEntityType.EMPTY);
    }
}
