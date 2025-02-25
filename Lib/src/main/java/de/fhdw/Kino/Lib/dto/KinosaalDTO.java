package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record KinosaalDTO(Long saalId, String saalName, KinoDTO kino, List<ReiheDTO> reihen) implements Serializable {}
