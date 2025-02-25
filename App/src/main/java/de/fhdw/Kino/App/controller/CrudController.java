package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.producer.CrudProducer;
import de.fhdw.Kino.Lib.dto.CrudRequest;
import de.fhdw.Kino.Lib.dto.CrudResponse;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class CrudController {

    @Autowired
    private CrudProducer crudProducer;

    @GetMapping
    public CrudResponse getTest() {
        return crudProducer.sendCrudRequest(new CrudRequest(new FilmDTO(1L, "Test"), CrudRequest.CrudOperation.CREATE));
    }
}