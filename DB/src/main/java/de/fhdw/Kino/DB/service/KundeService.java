package de.fhdw.Kino.DB.service;

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
        kunde.setVorname(dto.getVorname());
        kunde.setNachname(dto.getNachname());
        kunde.setEmail(dto.getEmail());
        kundeRepository.save(kunde);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "success", "kunde", kunde.toDTO());
    }

    @Transactional
    public CommandResponse handleKundeRequestAll() {
        Optional<Kunde> kunde = Optional.ofNullable(kundeRepository.findAll().get(0));
        return kunde.map(value -> new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "kundenListe", value.toDTO())).orElseGet(() -> new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kunden nicht gefunden", "error", null));


    }

}
