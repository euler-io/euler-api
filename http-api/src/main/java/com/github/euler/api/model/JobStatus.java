package com.github.euler.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets JobStatus
 */
public enum JobStatus {
	NEW("NEW"), ENQUEUED("ENQUEUED"), RUNNING("RUNNING"), ERROR("ERROR"), CANCELLING("CANCELLING"),
	CANCELLED("CANCELLED"), FINISHED("FINISHED");

	private String value;

	JobStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static JobStatus fromValue(String text) {
		for (JobStatus b : JobStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
