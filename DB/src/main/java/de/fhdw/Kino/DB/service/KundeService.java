package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Kunde;
import de.fhdw.Kino.DB.repository.KundeRepository;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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
    public CommandResponse handleKundeRequest(Long id) {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "found", "kunde", kundeRepository.findById(id).get().toDTO());
    }

    @Transactional
    public CommandResponse handleKundeRequestAll() {
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "kundenListe", kundeRepository.findAll().stream().map(Kunde::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleKundeDeletion(KundeDTO dto) {
        kundeRepository.deleteById(dto.getKundeId());
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"deleted", "null");
    }
}
