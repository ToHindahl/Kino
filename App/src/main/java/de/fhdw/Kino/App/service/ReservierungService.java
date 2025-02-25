package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.domain.Auffuehrung;
import de.fhdw.Kino.App.domain.Kunde;
import de.fhdw.Kino.App.domain.Reservierung;
import de.fhdw.Kino.App.domain.ReservierungsStatus;
import de.fhdw.Kino.App.dto.ReservierungRequest;
import de.fhdw.Kino.App.repository.AuffuehrungRepository;
import de.fhdw.Kino.App.repository.KundeRepository;
import de.fhdw.Kino.App.repository.ReservierungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ReservierungService {

    @Autowired
    private ReservierungRepository reservierungRepository;

    @Autowired
    private AuffuehrungRepository auffuehrungRepository;

    @Autowired
    private KundeRepository kundeRepository;

    public Reservierung createReservierung(ReservierungRequest request) {
        Optional<Auffuehrung> auffOpt = auffuehrungRepository.findById(request.getAuffuehrungId());
        Optional<Kunde> kundeOpt = kundeRepository.findById(request.getKundeId());
        if(auffOpt.isEmpty() || kundeOpt.isEmpty()) {
            throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Ungültige Aufführungs- oder Kunden-ID.", null, null, null);
        }
        Reservierung reservierung = new Reservierung();
        reservierung.setAuffuehrung(auffOpt.get());
        reservierung.setKunde(kundeOpt.get());
        reservierung.setSitzplatzIds(request.getSitzplatzIds());
        reservierung.setStatus(ReservierungsStatus.RESERVED);
        return reservierungRepository.save(reservierung);
    }

}
