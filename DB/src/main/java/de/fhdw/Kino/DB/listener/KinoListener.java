package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.domain.Kino;
import de.fhdw.Kino.DB.domain.Kinosaal;
import de.fhdw.Kino.DB.domain.Sitzreihe;
import de.fhdw.Kino.DB.domain.Sitzplatz;
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
public class KinoListener {

    private final KinoRepository kinoRepository;

    @Transactional
    @RabbitListener(queues = "kino.create.queue")
    public CreationResponseDTO handleKinoCreation(KinoDTO dto) {

        log.info("Kino creation request received");
        log.info("Kino: " + dto.name());

        if(!kinoRepository.findAll().isEmpty()) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Kino bereits initialisiert");
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

        return new CreationResponseDTO(kino.getKinoId(), StatusDTO.SUCCESS, "created");

    }

    @Transactional
    @RabbitListener(queues = "kino.get.queue")
    public GetKinoResponseDTO handleKinoRequest(GetKinoRequestDTO dto) {
        Optional<Kino> kino = Optional.ofNullable(kinoRepository.findAll().get(0));
        if (kino.isEmpty()) {
            return new GetKinoResponseDTO(null, StatusDTO.ERROR, "Kino nicht gefunden");
        }

        KinoDTO kinoDTO = new KinoDTO(kino.get().getKinoId(), kino.get().getName(), new ArrayList<>());

        kino.get().getKinosaele().forEach(kinosaal -> {
            KinosaalDTO kinosaalDTO = new KinosaalDTO(kinosaal.getKinosaalId(), kinosaal.getName(), kinoDTO, new ArrayList<>());
            kinosaal.getSitzreihen().forEach(reihe -> {
                SitzreiheDTO sitzreiheDTO = new SitzreiheDTO(reihe.getSitzreiheId(), reihe.getSitzreihenTypDTO(), kinosaalDTO, new ArrayList<>());

                reihe.getSitzplaetze().forEach(sitzplatz -> {
                    SitzplatzDTO sitzplatzDTO = new SitzplatzDTO(sitzplatz.getSitzplatzId(), sitzplatz.getNummer(), sitzreiheDTO);
                    sitzreiheDTO.sitzplaetze().add(sitzplatzDTO);
                });
                kinosaalDTO.sitzreihen().add(sitzreiheDTO);
            });
            kinoDTO.kinosaele().add(kinosaalDTO);
        });

        return new GetKinoResponseDTO(kinoDTO, StatusDTO.SUCCESS,"success");


    }

}
