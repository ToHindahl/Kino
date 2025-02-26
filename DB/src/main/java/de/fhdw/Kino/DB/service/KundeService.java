package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Kunde;
import de.fhdw.Kino.DB.repositories.KundeRepository;
import de.fhdw.Kino.Lib.dto.CommandResponse;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KundeService {

    private final KundeRepository kundeRepository;

    @Transactional
    public CommandResponse handleKundeCreation(KundeDTO dto) {
        Kunde kunde = new Kunde();
        kunde.setVorname(dto.vorname());
        kunde.setNachname(dto.nachname());
        kunde.setEmail(dto.email());
        kundeRepository.save(kunde);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "success", kunde.toDTO());
    }

}
