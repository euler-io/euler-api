package com.github.euler.api.persistence;

import java.io.IOException;

import com.github.euler.api.model.JobDetails;

public interface JobDetailsPersistence {

    public JobDetails get(String id) throws IOException;

    JobDetails create(JobDetails job) throws IOException;

}
