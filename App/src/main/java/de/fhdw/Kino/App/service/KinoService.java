package de.fhdw.Kino.App.service;

import de.fhdw.Kino.App.domain.Kino;
import de.fhdw.Kino.App.dto.KinoDto;
import de.fhdw.Kino.App.repository.KinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KinoService {

    @Autowired
    KinoRepository kinoRepository;

    public Kino createKino(KinoDto kinoDto){
        Kino kino = new Kino();
        kino.setId(kinoDto.getId());
        kino.setName(kinoDto.getName());
        kino.setSaele(kinoDto.getSaele());
        return kinoRepository.save(kino);
    }
}
