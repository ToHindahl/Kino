package de.fhdw.Kino.Lib.dto;

import de.fhdw.Kino.Lib.Kategorie;
import de.fhdw.Kino.Lib.Kinosaal;
import de.fhdw.Kino.Lib.Sitzplatz;
import de.fhdw.Kino.Lib.Sitzreihe;

import java.io.Serializable;
import java.util.List;

public class SitzreiheDTO implements Serializable, Sitzreihe {

    protected char nummer;
    protected Kategorie kategorie;
    protected Kinosaal kinosaal;


    @Override
    public char getNummer() {
        return this.nummer;
    }

    @Override
    public void setNummer(char value) {
        this.nummer = value;
    }

    @Override
    public Kategorie getKategorie() {
        return kategorie;
    }

    @Override
    public void setKategorie(Kategorie value) {
        this.kategorie = value;
    }

    @Override
    public Kinosaal getKinosaal() {
        return this.kinosaal;
    }

    @Override
    public void setKinosaal(Kinosaal value) {
        this.kinosaal = value;
    }

    @Override
    public List<Sitzplatz> getSitzplatz() {
        return List.of();
    }
}
