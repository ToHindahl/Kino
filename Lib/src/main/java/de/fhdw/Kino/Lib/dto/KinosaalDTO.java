package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;
import java.util.List;

public record KinosaalDTO(Long kinosaalId, String name, KinoDTO kino, List<SitzreiheDTO> sitzreihen) implements Serializable {}
