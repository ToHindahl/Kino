package de.fhdw.Kino.Lib;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Kategorie {

	PARKETT(10, "PARKETT", "PARKETT"),
	LOGE(12, "LOGE", "LOGE"),
	LOGE_MIT_SERVICE(15, "LOGE_MIT_SERVICE", "LOGE_MIT_SERVICE");


	public static final int PARKETT_VALUE = 10;
	public static final int LOGE_VALUE = 12;
	public static final int LOGE_MIT_SERVICE_VALUE = 15;

	private final int value;
	private final String name;
	private final String literal;

	private static final Kategorie[] VALUES_ARRAY = new Kategorie[] { PARKETT, LOGE, LOGE_MIT_SERVICE, };
	public static final List<Kategorie> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public String getLiteral() {
		return literal;
	}
	
	public static Kategorie get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Kategorie result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	public static Kategorie getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Kategorie result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	public static Kategorie get(int value) {
		switch (value) {
		case PARKETT_VALUE:
			return PARKETT;
		case LOGE_VALUE:
			return LOGE;
		case LOGE_MIT_SERVICE_VALUE:
			return LOGE_MIT_SERVICE;
		}
		return null;
	}

	private Kategorie(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	@Override
	public String toString() {
		return literal;
	}
}
