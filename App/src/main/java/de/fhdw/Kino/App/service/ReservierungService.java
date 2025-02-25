package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.producer.ReservierungProducer;
import de.fhdw.Kino.Lib.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservierungService {

    private final ReservierungProducer reservierungProducer;

    public ReservierungDTO createReservierung(ReservierungDTO dto) {

        CreationResponseDTO response = reservierungProducer.createReservierung(dto);

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return new ReservierungDTO(response.id(), dto.reservierungSitzplatzIds(), dto.reservierungAuffuehrungId(), dto.reservierungKundeId(), dto.reservierungStatus());

    }

    public BookReservierungResponseDTO bookReservierung(Long id) {

        BookReservierungResponseDTO response = reservierungProducer.bookReservierung(new BookReservierungRequestDTO(id));

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return response;
    }

    public CancelReservierungResponseDTO cancelReservierung(Long id) {

        CancelReservierungResponseDTO response = reservierungProducer.cancelReservierung(new CancelReservierungRequestDTO(id));

        if(response.status().equals(StatusDTO.ERROR)) {
            throw new RuntimeException(response.message());
        }

        return response;
    }


}
