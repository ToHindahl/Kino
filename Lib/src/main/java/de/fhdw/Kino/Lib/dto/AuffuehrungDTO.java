package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record AuffuehrungDTO(Long auffuehrungId, LocalDateTime startzeit, Long filmId, Long kinosaalId, List<Long> reservierteSitzplaetzeIds) implements Serializable {}
