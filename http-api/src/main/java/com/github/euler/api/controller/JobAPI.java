package com.github.euler.api.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobConfig;
import com.github.euler.api.model.JobDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "job", description = "Job API")
@SecurityRequirement(name = "bearerAuth")
public interface JobAPI {

	@Operation(summary = "Cancel the job execution.", operationId = "cancelJob")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "HTTP status code of the current operation."),
			@ApiResponse(responseCode = "400", description = "When the job can't be cancelled."),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/job/{job-id}", method = RequestMethod.DELETE)
	ResponseEntity<Void> cancelJob(
			@Parameter(description = "The job id.", required = true) @PathVariable("job-id") String jobId);

	@Operation(summary = "Create a new job.", operationId = "createNewJob")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON of the new job.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Job.class)) }),
			@ApiResponse(responseCode = "400", description = "Error creating the job."),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/job", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Job> createNewJob(
			@Parameter(description = "The JSON describing the job.", required = true) @Valid @RequestBody JobConfig body,
			@Parameter(description = "If the job must be automatically enqueued.") @Valid @RequestParam(value = "enqueue", required = false, defaultValue = "false") Boolean enqueue);

	@Operation(summary = "Enqueue a new job for execution.", operationId = "enqueueJob")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "HTTP status code of the current operation."),
			@ApiResponse(responseCode = "400", description = "When the job can't be enqueued for execution."),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/job/{job-id}", method = RequestMethod.POST)
	ResponseEntity<Void> enqueueJob(
			@Parameter(description = "The job id.", required = true) @PathVariable("job-id") String jobId);

	@Operation(summary = "Get the details of the job.", operationId = "getJobsDetails")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON of the job details.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JobDetails.class)) }),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/job/{job-id}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<JobDetails> getJobsDetails(
			@Parameter(description = "The job id.", required = true) @PathVariable("job-id") String jobId);

}
