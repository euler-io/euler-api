package com.github.euler.api.handler;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APICommand;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.typed.ActorSystem;

@Service
public class JobApiDelegateImpl implements JobApiDelegate {

    private final ActorSystem<APICommand> system;
    private final JobPersistence jobPersistence;
    private final JobDetailsPersistence jobDetailsPersistence;
    private final ObjectMapper objectMapper;

    @Autowired
    public JobApiDelegateImpl(ActorSystem<APICommand> system, JobPersistence jobPersistence, JobDetailsPersistence jobDetailsPersistence, ObjectMapper objectMapper) {
        super();
        this.system = system;
        this.jobPersistence = jobPersistence;
        this.jobDetailsPersistence = jobDetailsPersistence;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<Job> createNewJob(Object config) {
        String id = UUID.randomUUID().toString();
        try {
            enqueueJob(id, config);
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

    protected void enqueueJob(String id, Object raw) throws JsonProcessingException {
        String json = objectMapper.writer().writeValueAsString(raw);
        Config config = ConfigFactory.parseString(json);
        URI uri = null;
        JobToEnqueue msg = new JobToEnqueue(id, uri, config);
        system.tell(msg);
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
