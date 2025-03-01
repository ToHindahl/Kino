package de.fhdw.Kino.DB.service;

import de.fhdw.Kino.DB.model.Kunde;
import de.fhdw.Kino.DB.repository.KinoRepository;
import de.fhdw.Kino.DB.repository.KundeRepository;
import de.fhdw.Kino.DB.repository.ReservierungRepository;
import de.fhdw.Kino.Lib.dto.CommandResponse;
import de.fhdw.Kino.Lib.dto.KundeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KundeService {

    private final KundeRepository kundeRepository;

    private final KinoRepository kinoRepository;

    private final ReservierungRepository reservierungRepository;

    @Transactional
    public CommandResponse handleKundeCreation(KundeDTO dto) {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        Kunde kunde = new Kunde();
        kunde.setVorname(dto.getVorname());
        kunde.setNachname(dto.getNachname());
        kunde.setEmail(dto.getEmail());
        kundeRepository.save(kunde);
        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS, "success", "kunde", kunde.toDTO());
    }

    @Transactional
    public CommandResponse handleKundeRequestAll() {
        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"found", "kundenListe", kundeRepository.findAll().stream().map(Kunde::toDTO).toList());
    }

    @Transactional
    public CommandResponse handleKundeDeletion(Long id) {

        if(kinoRepository.findAll().isEmpty()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kino noch nicht initialisiert", "error", null);
        }

        if(reservierungRepository.findAll().stream().filter(reservierung -> reservierung.getKunde().getKundeId().equals(id)).findAny().isPresent()) {
            return new CommandResponse(CommandResponse.CommandStatus.ERROR, "Kunde hat Reservierungen", "error", null);
        }

        kundeRepository.deleteById(id);

        return new CommandResponse(CommandResponse.CommandStatus.SUCCESS,"deleted", "");
    }
}
