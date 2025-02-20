package de.fhdw.Kino.Lib;

import java.util.List;

public interface Film {

	String getTitel();
	void setTitel(String value);
	List<Auffuehrung> getAuffuehrung();

	int einnahmenBerechnen();
}
