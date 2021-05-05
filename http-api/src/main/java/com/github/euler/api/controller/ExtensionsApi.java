package com.github.euler.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.euler.api.model.Extension;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "extensions", description = "Extensions API.")
@SecurityRequirement(name = "bearerAuth")
public interface ExtensionsApi {

	@Operation(summary = "Get the installed extensions.", operationId = "getExtensions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A JSON array of installed extensions.", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Extension.class))) }),
			@ApiResponse(responseCode = "404", description = "Job not found"),
			@ApiResponse(responseCode = "401", description = "Access token is missing or invalid") })
	@RequestMapping(value = "/extensions", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Extension>> getExtensions();

}
