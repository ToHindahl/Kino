package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.KundeProducer;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KundeService {

    private final KundeProducer kundeProducer;

    public KundeDTO createKunde(KundeDTO kundeDto){

        CreationResponseDTO response = kundeProducer.createKunde(kundeDto);

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return new KundeDTO(response.id(), kundeDto.vorname(), kundeDto.nachname(), kundeDto.email());
    }
}
