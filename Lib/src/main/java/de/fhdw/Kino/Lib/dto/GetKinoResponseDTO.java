package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record GetKinoResponseDTO(KinoDTO kino, StatusDTO status, String message) implements Serializable {}
