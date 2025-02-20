package de.fhdw.Kino.Lib;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum KommandoTyp {

	ANFRAGEN(0, "ANFRAGEN", "ANFRAGEN"),
	PERSISTIEREN(1, "PERSISTIEREN", "PERSISTIEREN"),
	ABRUFEN(2, "ABRUFEN", "ABRUFEN"),
	STORNIEREN(3, "STORNIEREN", "STORNIEREN"),
	BUCHEN(4, "BUCHEN", "BUCHEN");

	public static final int ANFRAGEN_VALUE = 0;
	public static final int PERSISTIEREN_VALUE = 1;
	public static final int ABRUFEN_VALUE = 2;
	public static final int STORNIEREN_VALUE = 3;
	public static final int BUCHEN_VALUE = 4;

	private final int value;
	private final String name;
	private final String literal;

	private static final KommandoTyp[] VALUES_ARRAY = new KommandoTyp[] { ANFRAGEN, PERSISTIEREN, ABRUFEN, STORNIEREN,
			BUCHEN, };
	public static final List<KommandoTyp> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));


	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public String getLiteral() {
		return literal;
	}

	public static KommandoTyp get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			KommandoTyp result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	public static KommandoTyp getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			KommandoTyp result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	public static KommandoTyp get(int value) {
		switch (value) {
		case ANFRAGEN_VALUE:
			return ANFRAGEN;
		case PERSISTIEREN_VALUE:
			return PERSISTIEREN;
		case ABRUFEN_VALUE:
			return ABRUFEN;
		case STORNIEREN_VALUE:
			return STORNIEREN;
		case BUCHEN_VALUE:
			return BUCHEN;
		}
		return null;
	}

	private KommandoTyp(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	@Override
	public String toString() {
		return literal;
	}
}
