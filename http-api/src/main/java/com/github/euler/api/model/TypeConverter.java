package com.github.euler.api.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * TypeConverter
 */
@Validated
public class TypeConverter {
	@JsonProperty("type")
	private String type = null;

	@JsonProperty("config-type")
	private String configType = null;

	@JsonProperty("description")
	private String description = null;

	public TypeConverter type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Get type
	 * 
	 * @return type
	 **/
	@Schema
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TypeConverter configType(String configType) {
		this.configType = configType;
		return this;
	}

	/**
	 * Get configType
	 * 
	 * @return configType
	 **/
	@Schema

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public TypeConverter description(String description) {
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
		TypeConverter typeConverter = (TypeConverter) o;
		return Objects.equals(this.type, typeConverter.type)
				&& Objects.equals(this.configType, typeConverter.configType)
				&& Objects.equals(this.description, typeConverter.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, configType, description);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TypeConverter {\n");

		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    configType: ").append(toIndentedString(configType)).append("\n");
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
