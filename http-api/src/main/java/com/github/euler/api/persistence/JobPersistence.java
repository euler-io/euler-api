package com.github.euler.api.persistence;

import java.util.List;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;

public interface JobPersistence {

    List<Job> list();

    Job create(Job job);

    void updateStatus(String id, JobStatus status);

    void delete(String id);

}
