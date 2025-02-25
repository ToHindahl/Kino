package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllFilmResponseDTO(List<FilmDTO> filme, StatusDTO status, String message) implements Serializable {}
