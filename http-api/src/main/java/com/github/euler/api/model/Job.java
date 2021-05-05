package com.github.euler.api.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Job
 */
@Validated
public class Job {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("status")
	private JobStatus status = null;

	@JsonProperty("creation-date")
	private OffsetDateTime creationDate = null;

	@JsonProperty("enqueued-date")
	private OffsetDateTime enqueuedDate = null;

	@JsonProperty("start-date")
	private OffsetDateTime startDate = null;

	@JsonProperty("end-date")
	private OffsetDateTime endDate = null;

	public Job id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 **/
	@Schema

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Job status(JobStatus status) {
		this.status = status;
		return this;
	}

	/**
	 * Get status
	 * 
	 * @return status
	 **/
	@Schema

	@Valid
	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public Job creationDate(OffsetDateTime creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	/**
	 * Get creationDate
	 * 
	 * @return creationDate
	 **/
	@Schema

	@Valid
	public OffsetDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(OffsetDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Job enqueuedDate(OffsetDateTime enqueuedDate) {
		this.enqueuedDate = enqueuedDate;
		return this;
	}

	/**
	 * Get enqueuedDate
	 * 
	 * @return enqueuedDate
	 **/
	@Schema

	@Valid
	public OffsetDateTime getEnqueuedDate() {
		return enqueuedDate;
	}

	public void setEnqueuedDate(OffsetDateTime enqueuedDate) {
		this.enqueuedDate = enqueuedDate;
	}

	public Job startDate(OffsetDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * Get startDate
	 * 
	 * @return startDate
	 **/
	@Schema

	@Valid
	public OffsetDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(OffsetDateTime startDate) {
		this.startDate = startDate;
	}

	public Job endDate(OffsetDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * Get endDate
	 * 
	 * @return endDate
	 **/
	@Schema

	@Valid
	public OffsetDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(OffsetDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Job job = (Job) o;
		return Objects.equals(this.id, job.id) && Objects.equals(this.status, job.status)
				&& Objects.equals(this.creationDate, job.creationDate)
				&& Objects.equals(this.enqueuedDate, job.enqueuedDate) && Objects.equals(this.startDate, job.startDate)
				&& Objects.equals(this.endDate, job.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, status, creationDate, enqueuedDate, startDate, endDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Job {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
		sb.append("    enqueuedDate: ").append(toIndentedString(enqueuedDate)).append("\n");
		sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
		sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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
