package com.github.euler.api.controller;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.euler.api.APICommand;
import com.github.euler.api.JobToCancel;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.JobUtils;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobConfig;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.UserJobDetailsPersistence;

import akka.actor.typed.ActorSystem;

@RestController
public class JobController implements JobAPI {

	private final ActorSystem<APICommand> system;
	private final UserJobDetailsPersistence jobDetailsPersistence;

	@Autowired
	public JobController(ActorSystem<APICommand> system, UserJobDetailsPersistence jobDetailsPersistence) {
		super();
		this.system = system;
		this.jobDetailsPersistence = jobDetailsPersistence;
	}

	@Override
	public ResponseEntity<Void> cancelJob(String jobId) {
		system.tell(new JobToCancel(jobId));
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Job> createNewJob(JobConfig body, Boolean enqueue) {
		try {
			JobDetails jobDetails = new JobDetails();
			jobDetails.setCreationDate(OffsetDateTime.now());
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
