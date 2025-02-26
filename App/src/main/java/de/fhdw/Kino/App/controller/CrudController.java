package de.fhdw.Kino.App.controller;

import de.fhdw.Kino.App.producer.CommandProducer;
import de.fhdw.Kino.Lib.dto.CommandRequest;
import de.fhdw.Kino.Lib.dto.CommandResponse;
import de.fhdw.Kino.Lib.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class CrudController {

    @Autowired
    private CommandProducer crudProducer;

    @GetMapping
    public CommandResponse getTest() {
        return crudProducer.sendCommandRequest(new CommandRequest(new FilmDTO(1L, "Test"), CommandRequest.CommandType.CREATE));
    }
}