package de.fhdw.Kino.DB.listener;

import de.fhdw.Kino.DB.domain.Kunde;
import de.fhdw.Kino.DB.repositories.KundeRepository;
import de.fhdw.Kino.Lib.dto.CreationResponseDTO;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import de.fhdw.Kino.Lib.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KundeListener {

    private final KundeRepository kundeRepository;

    @Transactional
    @RabbitListener(queues = "kunde.create.queue")
    public CreationResponseDTO handleKundeCreation(KundeDTO dto) {
        Kunde kunde = new Kunde();
        kunde.setKundeVorname(dto.kundeVorname());
        kunde.setKundeNachname(dto.kundeNachname());
        kunde.setKundeEmail(dto.kundeEmail());
        kundeRepository.save(kunde);
        return new CreationResponseDTO(kunde.getKundeId(), StatusDTO.SUCCESS, "success");
    }

}
