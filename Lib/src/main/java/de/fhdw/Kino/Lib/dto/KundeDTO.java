package de.fhdw.Kino.Lib.dto;

import java.io.Serializable;

public record KundeDTO(Long kundeId, String kundeVorname, String kundeNachname, String kundeEmail)  implements Serializable {}
