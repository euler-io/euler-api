package com.github.euler.api.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ContextConverter
 */
@Validated
public class ContextConverter {
	@JsonProperty("path")
	private String path = null;

	@JsonProperty("description")
	private String description = null;

	public ContextConverter path(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Get path
	 * 
	 * @return path
	 **/
	@Schema
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ContextConverter description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Get description
	 * 
	 * @return description
	 **/
	@Schema
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContextConverter contextConverter = (ContextConverter) o;
		return Objects.equals(this.path, contextConverter.path)
				&& Objects.equals(this.description, contextConverter.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, description);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContextConverter {\n");

		sb.append("    path: ").append(toIndentedString(path)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
