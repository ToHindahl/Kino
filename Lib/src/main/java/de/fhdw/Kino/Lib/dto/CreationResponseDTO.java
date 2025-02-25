package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CreationResponseDTO(Long id, StatusDTO status, String message) implements Serializable {}
