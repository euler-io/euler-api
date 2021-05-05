package com.github.euler.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets SortBy
 */
public enum SortBy {
	CREATION_DATE("CREATION_DATE"), ENQUEUED_DATE("ENQUEUED_DATE"), START_DATE("START_DATE"), END_DATE("END_DATE");

	private String value;

	SortBy(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static SortBy fromValue(String text) {
		for (SortBy b : SortBy.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
