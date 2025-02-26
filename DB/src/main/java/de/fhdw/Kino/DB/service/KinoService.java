package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Kino;
import de.fhdw.Kino.DB.domain.Kinosaal;
import de.fhdw.Kino.DB.domain.Sitzplatz;
import de.fhdw.Kino.DB.domain.Sitzreihe;
import de.fhdw.Kino.DB.repositories.KinoRepository;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KinoService {

    private final KinoRepository kinoRepository;

    @Transactional
    public CommandResponse handleKinoCreation(KinoDTO dto) {
        if(!kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino bereits initialisiert", null);
        }

        Kino kino = new Kino();
        kino.setName(dto.name());

        dto.kinosaele().forEach(kinosaalDTO -> {
            Kinosaal kinosaal = new Kinosaal();
            kinosaal.setKino(kino);
            kinosaal.setName(kinosaalDTO.name());
            kinosaalDTO.sitzreihen().forEach(r -> {
                Sitzreihe sitzreihe = new Sitzreihe();
                sitzreihe.setKinosaal(kinosaal);
                switch (r.sitzreihenTyp()) {
                    case LOGE:
                        sitzreihe.setSitzreihenTyp(Sitzreihe.SitzreihenTyp.LOGE);
                    case PARKETT:
                        sitzreihe.setSitzreihenTyp(Sitzreihe.SitzreihenTyp.PARKETT);
                }
                r.sitzplaetze().forEach(s -> {
                    Sitzplatz sitzplatz = new Sitzplatz();
                    sitzplatz.setSitzreihe(sitzreihe);
                    sitzplatz.setNummer(s.nummer());
                    sitzreihe.getSitzplaetze().add(sitzplatz);
                });
                kinosaal.getSitzreihen().add(sitzreihe);
            });
            kino.getKinosaele().add(kinosaal);
        });

        kinoRepository.save(kino);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "created", kino.toDTO());

    }

    @Transactional
    public CommandResponse handleKinoRequest() {
        Optional<Kino> kino = Optional.ofNullable(kinoRepository.findAll().get(0));
        if (kino.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "Kino nicht gefunden", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"success", kino.get().toDTO());


    }

}
