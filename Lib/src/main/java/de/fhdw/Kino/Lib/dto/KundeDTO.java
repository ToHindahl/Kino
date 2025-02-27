package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record KundeDTO(Long kundeId, String vorname, String nachname, String email)  implements Serializable {}
