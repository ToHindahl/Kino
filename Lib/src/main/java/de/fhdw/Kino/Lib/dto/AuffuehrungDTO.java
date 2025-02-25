package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AuffuehrungDTO(Long auffuehrungId, LocalDateTime auffuehrungStartzeit, Long auffuehrungFilmId, Long auffuehrungSaalId) implements Serializable {}
