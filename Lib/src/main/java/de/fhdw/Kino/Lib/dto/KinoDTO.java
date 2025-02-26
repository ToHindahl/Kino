package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record KinoDTO(Long kinoId, String name, List<KinosaalDTO> kinosaele) implements Serializable {}
