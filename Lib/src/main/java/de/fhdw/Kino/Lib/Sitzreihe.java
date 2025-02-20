package de.fhdw.Kino.Lib;

import java.util.List;

public interface Sitzreihe {

	char getNummer();
	void setNummer(char value);
	Kategorie getKategorie();
	void setKategorie(Kategorie value);
	Kinosaal getKinosaal();
	void setKinosaal(Kinosaal value);
	List<Sitzplatz> getSitzplatz();
}
