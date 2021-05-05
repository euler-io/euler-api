package com.github.euler.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Token {

	@JsonProperty("access-token")
	private final String accessToken;

	@JsonProperty("token-type")
	private final String tokenType;

	public Token(String accessToken, String tokenType) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}

	public Token(String accessToken) {
		super();
		this.accessToken = accessToken;
		this.tokenType = "bearer";
	}

	@Schema
	public String getAccessToken() {
		return accessToken;
	}

	@Schema
	public String getTokenType() {
		return tokenType;
	}

}
