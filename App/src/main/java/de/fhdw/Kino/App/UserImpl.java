package de.fhdw.Kino.App;

import de.fhdw.Kino.Lib.User;

import java.io.Serializable;

public class UserImpl implements User, Serializable {

    private String vorname;
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getVorname() {
        return this.vorname;
    }

    @Override
    public void setVorname(String value) {
        this.vorname = value;
    }

}
