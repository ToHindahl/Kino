package de.fhdw.Kino.DB;

import de.fhdw.Kino.Lib.User;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserDTO implements User, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vorname;

    public UserDTO() {
        // Default constructor
    }

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