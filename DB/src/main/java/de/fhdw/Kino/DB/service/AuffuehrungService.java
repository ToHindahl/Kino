package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Auffuehrung;
import de.fhdw.Kino.DB.model.Film;
import de.fhdw.Kino.DB.model.Kinosaal;
import de.fhdw.Kino.DB.repository.AuffuehrungRepository;
import de.fhdw.Kino.DB.repository.FilmRepository;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.DB.repository.ReservierungRepository;
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

    private final ReservierungRepository reservierungRepository;

    @Transactional
    public CommandResponse handleAuffuehrungCreation(AuffuehrungDTO dto) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

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


        if(dto.getEndzeit().isBefore(dto.getStartzeit())){
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Die Endzeit darf nicht vor der Startzeit liegen.", "error");
        }

        if(auffuehrungRepository.findAll().stream().anyMatch(a -> a.getKinosaal().getKinosaalId().equals(dto.getKinosaalId()) && a.getStartzeit().isBefore(dto.getEndzeit()) && a.getEndzeit().isAfter(dto.getStartzeit()))) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Aufführung überschneidet sich mit einer anderen Aufführung", "error");
        }

        auffuehrung.setStartzeit(dto.getStartzeit());
        auffuehrung.setEndzeit(dto.getEndzeit());

        auffuehrungRepository.save(auffuehrung);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"created", "auffuehrung", auffuehrung.toDTO());
    }

    @Transactional
    public CommandResponse handleAuffuehrungRequestAll() {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "auffuehrungsListe", auffuehrungRepository.findAll().stream().map(Auffuehrung::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleAuffuehrungDeletion(Long id) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Optional<Auffuehrung> auffuehrung = auffuehrungRepository.findById(id);
        if(auffuehrung.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Aufführung nicht gefunden", "error");
        }

        if (!reservierungRepository.findAll().stream().filter(r -> r.getAuffuehrung().getAuffuehrungId().equals(id)).toList().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Aufführung kann nicht gelöscht werden, da es noch Reservierungen gibt", "error");
        }

        auffuehrungRepository.delete(auffuehrung.get());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "deleted", "");
    }
}
