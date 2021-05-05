package com.github.euler.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.euler.api.model.Token;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "auth", description = "The authentication API.")
public interface AuthAPI {

	@Operation(summary = "Generate a JWT Token.", operationId = "token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The generacted JWT token.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Token.class)) }),
			@ApiResponse(responseCode = "401", description = "Username or password incorrect.") })
	@PostMapping(value = "/token", produces = "application/json")
	ResponseEntity<Token> token(@Parameter(description = "The username", required = true) String username,
			@Parameter(description = "The password", required = true) String password);

}