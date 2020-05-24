package com.github.euler.api.persistence;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobPersistence;

@Service
public class ESJobPersistence implements JobPersistence {

    @Override
    public List<Job> list() {
        Job job = new Job();
        job.setId("0");
        job.setStatus(JobStatus.NEW);
        job.setStartDate(OffsetDateTime.now());
        return Arrays.asList(job);
    }

    @Override
    public Job create(Job job) {
        return job;
    }

    @Override
    public void updateStatus(String id, JobStatus status) {

    }

    @Override
    public void delete(String id) {

    }

}
