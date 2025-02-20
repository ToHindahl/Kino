package de.fhdw.Kino.Lib;

import java.util.Date;

public interface Auffuehrung {

	Date getDatum();
	void setDatum(Date value);
	Kinosaal getKinosaal();
	void setKinosaal(Kinosaal value);
	Film getFilm();
	void setFilm(Film value);
	Anfrage getAnfrage();
	void setAnfrage(Anfrage value);

	int einnahmenBerechnen();

	boolean kinosaalInBetrieb();
}
