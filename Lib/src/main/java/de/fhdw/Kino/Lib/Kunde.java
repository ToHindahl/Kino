package de.fhdw.Kino.Lib;

import java.util.List;

public interface Kunde {

	String getName();
	void setName(String value);
	String getVorname();
	void setVorname(String value);
	String getEmail();
	void setEmail(String value);
	List<Anfrage> getAnfrage();
}
