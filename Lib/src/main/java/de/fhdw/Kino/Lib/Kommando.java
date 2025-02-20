package de.fhdw.Kino.Lib;

public interface Kommando {

	KommandoTyp getTyp();
	void setTyp(KommandoTyp value);
	Object getObjekt();
	void setObjekt(Object value);
}
