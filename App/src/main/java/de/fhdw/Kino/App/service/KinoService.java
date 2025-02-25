package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.KinoProducer;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.KinoDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KinoService {

    private final KinoProducer kinoProducer;

    public KinoDTO createKino(KinoDTO kinoDto){

        CreationResponseDTO response = kinoProducer.createKino(kinoDto);

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return getKino();
    }

    public KinoDTO getKino() {
        return kinoProducer.getKino().kino();
    }
}
