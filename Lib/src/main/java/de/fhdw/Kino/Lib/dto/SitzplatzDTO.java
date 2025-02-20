package de.fhdw.Kino.Lib.dto;

import de.fhdw.Kino.Lib.Sitzplatz;
import de.fhdw.Kino.Lib.Sitzreihe;

import java.io.Serializable;

public class SitzplatzDTO implements Serializable, Sitzplatz {
    @Override
    public int getNummer() {
        return 0;
    }

    @Override
    public void setNummer(int value) {

    }

    @Override
    public Sitzreihe getSitzreihe() {
        return null;
    }

    @Override
    public void setSitzreihe(Sitzreihe value) {

    }
}
