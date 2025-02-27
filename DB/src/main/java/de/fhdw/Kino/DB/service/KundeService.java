package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.domain.Kino;
import de.fhdw.Kino.DB.domain.Kunde;
import de.fhdw.Kino.DB.repositories.KundeRepository;
import de.fhdw.Kino.Lib.dto.CommandResponse;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "success", "kunde", kunde.toDTO());
    }

    @Transactional
    public CommandResponse handleKinoRequest() {
        Optional<Kunde> kunde = Optional.ofNullable(kundeRepository.findAll().get(0));
        if (kunde.isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "Kino nicht gefunden", "error", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"success", "kunde", kunde.get().toDTO());


    }

}
