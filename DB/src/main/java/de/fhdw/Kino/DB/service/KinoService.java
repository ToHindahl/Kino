package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Kino;
import de.fhdw.Kino.DB.model.Kinosaal;
import de.fhdw.Kino.DB.model.Sitzplatz;
import de.fhdw.Kino.DB.model.Sitzreihe;
import de.fhdw.Kino.DB.repository.*;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class KinoService {

    private final KinoRepository kinoRepository;
    private final KundeRepository kundeRepository;
    private final ReservierungRepository reservierungRepository;
    private final AuffuehrungRepository auffuehrungRepository;
    private final FilmRepository filmRepository;

    @Transactional
    public CommandResponse handleKinoCreation(KinoDTO dto) {

        Kino kino = new Kino();
        kino.setName(dto.getName());

        dto.getKinosaele().forEach(kinosaalDTO -> {
            Kinosaal kinosaal = new Kinosaal();
            kinosaal.setKino(kino);
            kinosaal.setName(kinosaalDTO.getName());
            kinosaalDTO.getSitzreihen().forEach(r -> {
                Sitzreihe sitzreihe = new Sitzreihe();
                sitzreihe.setKinosaal(kinosaal);
                sitzreihe.setBezeichnung(r.getBezeichnung());
                switch (r.getSitzreihenTyp()) {
                    case LOGE_MIT_SERVICE -> sitzreihe.setSitzreihenTyp(Sitzreihe.SitzreihenTyp.LOGE_MIT_SERVICE);
                    case LOGE -> sitzreihe.setSitzreihenTyp(Sitzreihe.SitzreihenTyp.LOGE);
                    case PARKETT -> sitzreihe.setSitzreihenTyp(Sitzreihe.SitzreihenTyp.PARKETT);
                }
                r.getSitzplaetze().forEach(s -> {
                    Sitzplatz sitzplatz = new Sitzplatz();
                    sitzplatz.setSitzreihe(sitzreihe);
                    sitzplatz.setNummer(s.getNummer());
                    sitzreihe.getSitzplaetze().add(sitzplatz);
                });
                kinosaal.getSitzreihen().add(sitzreihe);
            });
            kino.getKinosaele().add(kinosaal);
        });

        kinoRepository.save(kino);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created","kino", kino.toDTO());
    }

    @Transactional
    public CommandResponse handleKinoRequest() {
        Optional<Kino> kino = kinoRepository.findAll().stream().findFirst();
        return kino.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "kino", value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "null", "null"));
    }

    @Transactional
    public CommandResponse handleKinoReset() {

        kinoRepository.deleteAll();
        kundeRepository.deleteAll();
        reservierungRepository.deleteAll();
        auffuehrungRepository.deleteAll();
        filmRepository.deleteAll();
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "reset", "null");
    }
}
