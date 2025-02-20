package de.fhdw.Kino.Lib;

import java.util.List;

public interface Kino {

	String getName();
	void setName(String value);
	List<Kinosaal> getKinosaal();
	List<Film> getFilm();
	List<Kunde> getKunde();
}
