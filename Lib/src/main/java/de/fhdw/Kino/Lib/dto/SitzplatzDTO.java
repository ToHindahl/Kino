package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record SitzplatzDTO(Long sitzplatzId, int sitzplatzNummer, ReiheDTO sitzplatzReihe) implements Serializable {}
