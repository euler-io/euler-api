package com.github.euler.api.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APICommand;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobConfig;
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
    public ResponseEntity<Job> createNewJob(JobConfig body) {
        String id = UUID.randomUUID().toString();
        try {
            JobDetails jobDetails = new JobDetails();
            jobDetails.setId(id);
            jobDetails.setStartDate(OffsetDateTime.now());
            jobDetails.setStatus(JobStatus.NEW);
            jobDetails.setConfig(body.getConfig());
            jobDetails.setSeed(body.getSeed());
            Job job = JobUtils.fromDetails(jobDetailsPersistence.create(jobDetails));
            return new ResponseEntity<Job>(job, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> cancelJob(String jobId) {
        // TODO Auto-generated method stub
        return JobApiDelegate.super.cancelJob(jobId);
    }

    @Override
    public ResponseEntity<Void> enqueueJob(String jobId) {
        try {
            JobDetails jobDetails = jobDetailsPersistence.get(jobId);
            Object rawConfig = jobDetails.getConfig();
            String json = objectMapper.writer().writeValueAsString(rawConfig);
            URI uri = new URI(jobDetails.getSeed());
            Config config = ConfigFactory.parseString(json);
            JobToEnqueue msg = new JobToEnqueue(jobId, uri, config);
            system.tell(msg);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
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
