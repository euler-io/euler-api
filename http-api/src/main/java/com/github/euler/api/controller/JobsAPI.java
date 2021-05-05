package com.github.euler.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.euler.api.model.JobList;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "job", description = "Job API.")
@SecurityRequirement(name = "bearerAuth")
public interface JobsAPI {

	@Operation(summary = "Get a list of jobs.", operationId = "listJobs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A list of jobs.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JobList.class)) }),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "403", description = "Access forbidden", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }) })
	@GetMapping(value = "/jobs", produces = "application/json")
	ResponseEntity<JobList> listJobs(
			@Parameter(description = "The page number.") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@Max(1000) @Parameter(description = "The page size.") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@Parameter(description = "The field to sort by.") @Valid @RequestParam(value = "sort-by", required = false) SortBy sortBy,
			@Parameter(description = "The direction of the sorting.") @Valid @RequestParam(value = "sort-direction", required = false) SortDirection sortDirection,
			@Parameter(description = "The status to be used as filter.") @Valid @RequestParam(value = "status", required = false) JobStatus status);

}
