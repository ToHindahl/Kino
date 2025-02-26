package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.AuffuehrungProducer;
import de.fhdw.Kino.Lib.dto.AuffuehrungDTO;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuffuehrungService {

    private final AuffuehrungProducer auffuehrungProducer;

    public AuffuehrungDTO createAuffuehrung(AuffuehrungDTO auffuehrungDto){

        CreationResponseDTO response = auffuehrungProducer.createAuffuehrung(auffuehrungDto);

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return new AuffuehrungDTO(response.id(), auffuehrungDto.startzeit(), auffuehrungDto.filmId(), auffuehrungDto.kinosaalId());
    }

    public List<AuffuehrungDTO> getAllAuffuehrungen() {
        return auffuehrungProducer.getAllAuffuehrungen().auffuehrungen();
    }

}
