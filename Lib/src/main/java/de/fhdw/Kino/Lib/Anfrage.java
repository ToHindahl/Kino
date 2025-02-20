package de.fhdw.Kino.Lib;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface Anfrage {

	LocalDate getDatum();
	void setDatum(LocalDate value);
	Auffuehrung getAuffuehrung();
	void setAuffuehrung(Auffuehrung value);
	List<Sitzplatz> getGewaehlteSitzplaetze();
}
