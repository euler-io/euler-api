package com.github.euler.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets SortDirection
 */
public enum SortDirection {
	ASC("ASC"), DESC("DESC");

	private String value;

	SortDirection(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static SortDirection fromValue(String text) {
		for (SortDirection b : SortDirection.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
