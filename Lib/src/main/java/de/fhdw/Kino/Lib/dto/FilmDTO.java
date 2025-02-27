package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record FilmDTO(Long filmId, String titel) implements Serializable {}
