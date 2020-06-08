package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.List;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

public interface JobPersistence {

    List<Job> list(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection) throws IOException;

    void updateStatus(String id, JobStatus status) throws IOException;

    Job get(String id) throws IOException;

    void delete(String id) throws IOException;

}
