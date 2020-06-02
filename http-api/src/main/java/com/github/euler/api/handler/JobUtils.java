package com.github.euler.api.handler;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;

public class JobUtils {

    public static Job fromDetails(JobDetails details) {
        Job job = new Job();
        job.setId(details.getId());
        job.setStatus(details.getStatus());
        job.setStartDate(details.getStartDate());
        job.setEndDate(details.getEndDate());
        return job;
    }

}
