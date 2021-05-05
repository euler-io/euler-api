package com.github.euler.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.euler.api.model.Token;

@RestController
public class AuthController implements AuthAPI {

	@Override
	public ResponseEntity<Token> token(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		// Intercepted by JWTAuthenticationFilter
		return new ResponseEntity<Token>(HttpStatus.NOT_IMPLEMENTED);
	}

}
