package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record CancelReservierungResponseDTO(Long reservierungId, StatusDTO status, String message) implements Serializable {}
