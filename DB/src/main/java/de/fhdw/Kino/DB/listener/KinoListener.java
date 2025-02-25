package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.domain.Kino;
import de.fhdw.Kino.DB.domain.Kinosaal;
import de.fhdw.Kino.DB.domain.Reihe;
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
        log.info("Kino: " + dto.kinoName());

        if(!kinoRepository.findAll().isEmpty()) {
            return new CreationResponseDTO(-1L, StatusDTO.ERROR, "Kino bereits initialisiert");
        }

        Kino kino = new Kino();
        kino.setKinoName(dto.kinoName());

        dto.kinoSaele().forEach(kinosaalDTO -> {
            Kinosaal kinosaal = new Kinosaal();
            kinosaal.setKino(kino);
            kinosaal.setKinosaalName(kinosaalDTO.saalName());
            kinosaalDTO.reihen().forEach(r -> {
                Reihe reihe = new Reihe();
                reihe.setReiheSaal(kinosaal);
                switch (r.reiheTyp()) {
                    case LOGE:
                        reihe.setReiheTyp(Reihe.ReihenTyp.LOGE);
                    case PARKETT:
                        reihe.setReiheTyp(Reihe.ReihenTyp.PARKETT);
                }
                r.reiheSitze().forEach(s -> {
                    Sitzplatz sitzplatz = new Sitzplatz();
                    sitzplatz.setSitzplatzReihe(reihe);
                    sitzplatz.setSitzplatzNummer(s.sitzplatzNummer());
                    reihe.getReiheSitze().add(sitzplatz);
                });
                kinosaal.getKinosaalReihen().add(reihe);
            });
            kino.getKinoSaele().add(kinosaal);
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

        KinoDTO kinoDTO = new KinoDTO(kino.get().getKinoId(), kino.get().getKinoName(), new ArrayList<>());

        kino.get().getKinoSaele().forEach(kinosaal -> {
            KinosaalDTO kinosaalDTO = new KinosaalDTO(kinosaal.getKinosaalId(), kinosaal.getKinosaalName(), kinoDTO, new ArrayList<>());
            kinosaal.getKinosaalReihen().forEach(reihe -> {
                ReiheDTO reiheDTO = new ReiheDTO(reihe.getReiheId(), reihe.getReiheTypDTO(), kinosaalDTO, new ArrayList<>());

                reihe.getReiheSitze().forEach(sitzplatz -> {
                    SitzplatzDTO sitzplatzDTO = new SitzplatzDTO(sitzplatz.getSitzplatzId(), sitzplatz.getSitzplatzNummer(), reiheDTO);
                    reiheDTO.reiheSitze().add(sitzplatzDTO);
                });
                kinosaalDTO.reihen().add(reiheDTO);
            });
            kinoDTO.kinoSaele().add(kinosaalDTO);
        });

        return new GetKinoResponseDTO(kinoDTO, StatusDTO.SUCCESS,"success");


    }

}
