package com.github.euler.api.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JobStatistics
 */
@Validated
public class JobStatistics {
	@JsonProperty("items-found")
	private Integer itemsFound = null;

	@JsonProperty("items-processed")
	private Integer itemsProcessed = null;

	@JsonProperty("timestamp")
	private OffsetDateTime timestamp = null;

	public JobStatistics itemsFound(Integer itemsFound) {
		this.itemsFound = itemsFound;
		return this;
	}

	/**
	 * Get itemsFound
	 * 
	 * @return itemsFound
	 **/
	@Schema
	public Integer getItemsFound() {
		return itemsFound;
	}

	public void setItemsFound(Integer itemsFound) {
		this.itemsFound = itemsFound;
	}

	public JobStatistics itemsProcessed(Integer itemsProcessed) {
		this.itemsProcessed = itemsProcessed;
		return this;
	}

	/**
	 * Get itemsProcessed
	 * 
	 * @return itemsProcessed
	 **/
	@Schema
	public Integer getItemsProcessed() {
		return itemsProcessed;
	}

	public void setItemsProcessed(Integer itemsProcessed) {
		this.itemsProcessed = itemsProcessed;
	}

	public JobStatistics timestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * Get timestamp
	 * 
	 * @return timestamp
	 **/
	@Schema
	@Valid
	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JobStatistics jobStatistics = (JobStatistics) o;
		return Objects.equals(this.itemsFound, jobStatistics.itemsFound)
				&& Objects.equals(this.itemsProcessed, jobStatistics.itemsProcessed)
				&& Objects.equals(this.timestamp, jobStatistics.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemsFound, itemsProcessed, timestamp);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class JobStatistics {\n");

		sb.append("    itemsFound: ").append(toIndentedString(itemsFound)).append("\n");
		sb.append("    itemsProcessed: ").append(toIndentedString(itemsProcessed)).append("\n");
		sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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
