package de.fhdw.Kino.Lib;

import java.util.List;

public interface Kinosaal {

	int getNummer();
	void setNummer(int value);
	boolean isInBetrieb();
	void setInBetrieb(boolean value);
	List<Sitzreihe> getSitzreihe();
}
