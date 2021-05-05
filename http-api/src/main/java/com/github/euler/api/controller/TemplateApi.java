package com.github.euler.api.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.euler.api.model.Job;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateConfig;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "template", description = "Template API.")
@SecurityRequirement(name = "bearerAuth")
public interface TemplateApi {

	@Operation(summary = "Create a new template.", operationId = "createNewTemplate")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON of the new template.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Template.class)) }),
			@ApiResponse(responseCode = "400", description = "Error creating the template."),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/template", produces = { "application/json" }, consumes = {
			"application/hocon" }, method = RequestMethod.PUT)
	ResponseEntity<Template> createNewTemplate(
			@Parameter(description = "The JSON describing the template. If a template with this name already exists, it will be replaced.", required = true) @Valid @RequestBody TemplateConfig body);

	@Operation(summary = "Delete the template.", operationId = "deleteTemplate")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "HTTP status responseCode of the current operation."),
			@ApiResponse(responseCode = "400", description = "When the job can't be deleted."),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/template/{template-name}", method = RequestMethod.DELETE)
	ResponseEntity<Void> deleteTemplate(
			@Parameter(description = "The template name.", required = true) @PathVariable("template-name") String templateName);

	@Operation(summary = "Enqueue a template as a new job for execution.", operationId = "enqueueTemplate")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON of the new job.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Job.class)) }),
			@ApiResponse(responseCode = "400", description = "When the template can't be enqueued for execution."),
			@ApiResponse(responseCode = "404", description = "Template not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/template/{template-name}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Job> enqueueTemplate(
			@Parameter(description = "The template name.", required = true) @PathVariable("template-name") String templateName,
			@Parameter(description = "The JSON describing the template parameters.", required = true) @Valid @RequestBody TemplateParams body);

	@Operation(summary = "Get the details of the template.", operationId = "getTemplateDetails")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON of the template details.", content = {
					@Content(mediaType = "application/hocon", schema = @Schema(implementation = TemplateDetails.class)) }),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/template/{template-name}", produces = { "application/hocon" }, method = RequestMethod.GET)
	ResponseEntity<TemplateDetails> getTemplateDetails(
			@Parameter(description = "The template name.", required = true) @PathVariable("template-name") String templateName);

}
