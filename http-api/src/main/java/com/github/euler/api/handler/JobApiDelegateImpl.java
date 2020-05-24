package com.github.euler.api.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;

@Service
public class JobApiDelegateImpl implements JobApiDelegate {

    private final JobPersistence jobPersistence;
    private final JobDetailsPersistence jobDetailsPersistence;

    @Autowired
    public JobApiDelegateImpl(JobPersistence jobPersistence, JobDetailsPersistence jobDetailsPersistence) {
        super();
        this.jobPersistence = jobPersistence;
        this.jobDetailsPersistence = jobDetailsPersistence;
    }

    @Override
    public ResponseEntity<Job> createNewJob(Object body) {
        return JobApiDelegate.super.createNewJob(body);
    }

    @Override
    public ResponseEntity<Void> deleteJob(String jobId) {
        return JobApiDelegate.super.deleteJob(jobId);
    }

    @Override
    public ResponseEntity<Job> changeJobStatus(String jobId, JobStatus status) {
        return JobApiDelegate.super.changeJobStatus(jobId, status);
    }

    @Override
    public ResponseEntity<JobDetails> getJobsDetails(String jobId) {
        return JobApiDelegate.super.getJobsDetails(jobId);
    }

}
