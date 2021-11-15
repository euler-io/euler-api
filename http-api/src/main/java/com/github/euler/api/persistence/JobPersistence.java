package com.github.euler.api.persistence;

import java.io.IOException;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobList;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

public interface JobPersistence {

    JobList list(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection, JobStatus[] status, String[] tag) throws IOException;

    void updateStatus(String id, JobStatus status) throws IOException;

    Job get(String id) throws IOException;

    void delete(String id) throws IOException;

    void updateFinished(String id) throws IOException;

    void updateEnqueued(String id) throws IOException;

    void updateRunning(String id) throws IOException;

}
