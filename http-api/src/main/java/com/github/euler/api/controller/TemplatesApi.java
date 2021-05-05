package com.github.euler.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.euler.api.model.TemplateList;

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
public interface TemplatesApi {

	@Operation(summary = "Get a list of templates.", operationId = "listTemplates")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A list of templates.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = TemplateList.class)) }),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/templates", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<TemplateList> listTemplates(
			@Parameter(description = "The page number.") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@Max(1000) @Parameter(description = "The page size.") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@Parameter(description = "The name or part of the name of the template.") @Valid @RequestParam(value = "name", required = false) String name);

}
