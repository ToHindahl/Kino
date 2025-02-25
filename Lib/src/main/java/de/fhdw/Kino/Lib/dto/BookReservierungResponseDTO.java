package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record BookReservierungResponseDTO(Long reservierungId, StatusDTO status, String message) implements Serializable {}