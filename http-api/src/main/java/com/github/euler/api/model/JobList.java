package com.github.euler.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JobList
 */
@Validated
public class JobList {
	@JsonProperty("total")
	private Integer total = null;

	@JsonProperty("jobs")
	@Valid
	private List<Job> jobs = null;

	public JobList total(Integer total) {
		this.total = total;
		return this;
	}

	/**
	 * Get total
	 * 
	 * @return total
	 **/
	@Schema
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public JobList jobs(List<Job> jobs) {
		this.jobs = jobs;
		return this;
	}

	public JobList addJobsItem(Job jobsItem) {
		if (this.jobs == null) {
			this.jobs = new ArrayList<>();
		}
		this.jobs.add(jobsItem);
		return this;
	}

	/**
	 * Get jobs
	 * 
	 * @return jobs
	 **/
	@Schema
	@Valid
	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JobList jobList = (JobList) o;
		return Objects.equals(this.total, jobList.total) && Objects.equals(this.jobs, jobList.jobs);
	}

	@Override
	public int hashCode() {
		return Objects.hash(total, jobs);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class JobList {\n");

		sb.append("    total: ").append(toIndentedString(total)).append("\n");
		sb.append("    jobs: ").append(toIndentedString(jobs)).append("\n");
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
