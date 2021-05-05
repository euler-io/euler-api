package com.github.euler.api;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;

public class JobUtils {

	public static Job fromDetails(JobDetails details) {
		Job job = new Job();
		job.setId(details.getId());
		job.setStatus(details.getStatus());
		job.setCreationDate(details.getCreationDate());
		job.setEndDate(details.getEndDate());
		return job;
	}

}
