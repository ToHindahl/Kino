package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.domain.Kunde;
import de.fhdw.Kino.App.dto.KundeDto;
import de.fhdw.Kino.App.repository.KundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundeService {

    @Autowired
    private KundeRepository kundeRepository;

    public Kunde createKunde(KundeDto kundeDto){
        Kunde kunde = new Kunde();
        kunde.setId(kundeDto.getId());
        kunde.setVorname(kundeDto.getVorname());
        kunde.setNachname(kundeDto.getNachname());
        kunde.setEmail(kundeDto.getEmail());
        return kundeRepository.save(kunde);
    }
}
