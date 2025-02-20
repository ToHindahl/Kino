package de.fhdw.Kino.Lib.dto;

import de.fhdw.Kino.Lib.Anfrage;
import de.fhdw.Kino.Lib.Auffuehrung;
import de.fhdw.Kino.Lib.Sitzplatz;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public abstract class AnfrageDTO implements Serializable, Anfrage {

    protected LocalDate datum;
    protected Auffuehrung auffuehrung;
    protected List<Sitzplatz> gewaehlteSitzplaetze;

    @Override
    public LocalDate getDatum() {
        return this.datum;
    }

    @Override
    public void setDatum(LocalDate value) {
        this.datum = value;
    }

    @Override
    public Auffuehrung getAuffuehrung() {
        return this.auffuehrung;
    }

    @Override
    public void setAuffuehrung(Auffuehrung value) {
        this.auffuehrung = value;
    }

    @Override
    public List<Sitzplatz> getGewaehlteSitzplaetze() {
        return this.gewaehlteSitzplaetze;
    }
}
