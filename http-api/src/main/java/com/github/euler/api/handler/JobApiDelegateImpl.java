package com.github.euler.api.handler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.APICommand;
import com.github.euler.api.JobToCancel;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobConfig;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;

import akka.actor.typed.ActorSystem;

@Service
public class JobApiDelegateImpl implements JobApiDelegate {

    private final ActorSystem<APICommand> system;
    private final JobDetailsPersistence jobDetailsPersistence;

    @Autowired
    public JobApiDelegateImpl(ActorSystem<APICommand> system, JobDetailsPersistence jobDetailsPersistence) {
        super();
        this.system = system;
        this.jobDetailsPersistence = jobDetailsPersistence;
    }

    @Override
    public ResponseEntity<Job> createNewJob(JobConfig body, Boolean enqueue) {
        String id = UUID.randomUUID().toString();
        try {
            JobDetails jobDetails = new JobDetails();
            jobDetails.setId(id);
            jobDetails.setStartDate(OffsetDateTime.now());
            jobDetails.setStatus(JobStatus.NEW);
            jobDetails.setConfig(body.getConfig());
            jobDetails.setSeed(body.getSeed());
            Job job = JobUtils.fromDetails(jobDetailsPersistence.create(jobDetails));
            if (enqueue) {
                enqueueJob(job.getId());
            }
            return new ResponseEntity<Job>(job, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> cancelJob(String jobId) {
        system.tell(new JobToCancel(jobId));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> enqueueJob(String jobId) {
        JobToEnqueue msg = new JobToEnqueue(jobId);
        system.tell(msg);
        return new ResponseEntity<Void>(HttpStatus.OK);
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
