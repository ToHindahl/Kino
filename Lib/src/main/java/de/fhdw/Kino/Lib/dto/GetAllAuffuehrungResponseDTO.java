package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllAuffuehrungResponseDTO(List<AuffuehrungDTO> auffuehrungen, StatusDTO status, String message) implements Serializable {}
