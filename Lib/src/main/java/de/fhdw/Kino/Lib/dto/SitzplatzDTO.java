package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record SitzplatzDTO(Long sitzplatzId, int nummer, SitzreiheDTO sitzreihe) implements Serializable {}
