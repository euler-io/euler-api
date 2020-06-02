package com.github.euler.api.handler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Service
public class JobApiDelegateImpl implements JobApiDelegate {

    private final JobPersistence jobPersistence;
    private final JobDetailsPersistence jobDetailsPersistence;
    private final ObjectMapper objectMapper;

    @Autowired
    public JobApiDelegateImpl(JobPersistence jobPersistence, JobDetailsPersistence jobDetailsPersistence, ObjectMapper objectMapper) {
        super();
        this.jobPersistence = jobPersistence;
        this.jobDetailsPersistence = jobDetailsPersistence;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<Job> createNewJob(Object config) {
        String id = UUID.randomUUID().toString();
        try {
            startJob(id, config);
            JobDetails jobDetails = new JobDetails();
            jobDetails.setId(id);
            jobDetails.setStartDate(OffsetDateTime.now());
            jobDetails.setStatus(JobStatus.NEW);
            jobDetails.setConfig(config);
            Job job = JobUtils.fromDetails(jobDetailsPersistence.create(jobDetails));
            return new ResponseEntity<Job>(job, HttpStatus.OK);
        } catch (IOException e) {
            stopJob(id);
            throw new RuntimeException(e);
        }
    }

    protected void startJob(String id, Object raw) throws JsonProcessingException {
        String json = objectMapper.writer().writeValueAsString(raw);
        Config config = ConfigFactory.parseString(json);
        System.out.println(config);
    }

    protected void stopJob(String id) {

    }

    @Override
    public ResponseEntity<Void> deleteJob(String jobId) {
        try {
            jobPersistence.delete(jobId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> changeJobStatus(String jobId, JobStatus status) {
        try {
            jobPersistence.updateStatus(jobId, status);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<JobDetails> getJobsDetails(String jobId) {
        try {
            JobDetails jobDetails = jobDetailsPersistence.get(jobId);
            return new ResponseEntity<JobDetails>(jobDetails, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
