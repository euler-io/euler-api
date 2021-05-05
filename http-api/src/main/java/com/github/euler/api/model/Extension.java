package com.github.euler.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Extension
 */
@Validated
public class Extension {
	@JsonProperty("description")
	private String description = null;

	@JsonProperty("path-converters")
	@Valid
	private List<ContextConverter> pathConverters = null;

	@JsonProperty("type-converters")
	@Valid
	private List<TypeConverter> typeConverters = null;

	@JsonProperty("task-converters")
	@Valid
	private List<TaskConverter> taskConverters = null;

	public Extension description(String description) {
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

	public Extension pathConverters(List<ContextConverter> pathConverters) {
		this.pathConverters = pathConverters;
		return this;
	}

	public Extension addPathConvertersItem(ContextConverter pathConvertersItem) {
		if (this.pathConverters == null) {
			this.pathConverters = new ArrayList<>();
		}
		this.pathConverters.add(pathConvertersItem);
		return this;
	}

	/**
	 * Get pathConverters
	 * 
	 * @return pathConverters
	 **/
	@Schema
	@Valid
	public List<ContextConverter> getPathConverters() {
		return pathConverters;
	}

	public void setPathConverters(List<ContextConverter> pathConverters) {
		this.pathConverters = pathConverters;
	}

	public Extension typeConverters(List<TypeConverter> typeConverters) {
		this.typeConverters = typeConverters;
		return this;
	}

	public Extension addTypeConvertersItem(TypeConverter typeConvertersItem) {
		if (this.typeConverters == null) {
			this.typeConverters = new ArrayList<>();
		}
		this.typeConverters.add(typeConvertersItem);
		return this;
	}

	/**
	 * Get typeConverters
	 * 
	 * @return typeConverters
	 **/
	@Schema
	@Valid
	public List<TypeConverter> getTypeConverters() {
		return typeConverters;
	}

	public void setTypeConverters(List<TypeConverter> typeConverters) {
		this.typeConverters = typeConverters;
	}

	public Extension taskConverters(List<TaskConverter> taskConverters) {
		this.taskConverters = taskConverters;
		return this;
	}

	public Extension addTaskConvertersItem(TaskConverter taskConvertersItem) {
		if (this.taskConverters == null) {
			this.taskConverters = new ArrayList<>();
		}
		this.taskConverters.add(taskConvertersItem);
		return this;
	}

	/**
	 * Get taskConverters
	 * 
	 * @return taskConverters
	 **/
	@Schema
	@Valid
	public List<TaskConverter> getTaskConverters() {
		return taskConverters;
	}

	public void setTaskConverters(List<TaskConverter> taskConverters) {
		this.taskConverters = taskConverters;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Extension extension = (Extension) o;
		return Objects.equals(this.description, extension.description)
				&& Objects.equals(this.pathConverters, extension.pathConverters)
				&& Objects.equals(this.typeConverters, extension.typeConverters)
				&& Objects.equals(this.taskConverters, extension.taskConverters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, pathConverters, typeConverters, taskConverters);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Extension {\n");

		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    pathConverters: ").append(toIndentedString(pathConverters)).append("\n");
		sb.append("    typeConverters: ").append(toIndentedString(typeConverters)).append("\n");
		sb.append("    taskConverters: ").append(toIndentedString(taskConverters)).append("\n");
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
