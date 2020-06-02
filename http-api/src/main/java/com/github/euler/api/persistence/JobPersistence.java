package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.List;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;

public interface JobPersistence {

    List<Job> list() throws IOException;

    void updateStatus(String id, JobStatus status) throws IOException;

    void delete(String id) throws IOException;

}
