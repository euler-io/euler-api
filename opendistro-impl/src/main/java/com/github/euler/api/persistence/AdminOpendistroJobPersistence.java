package com.github.euler.api.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroConfiguration;

@Service
public class AdminOpendistroJobPersistence extends OpendistroJobPersistence implements AdminJobPersistence {

	@Autowired
	public AdminOpendistroJobPersistence(OpenDistroConfiguration openDistroConfiguration,
			APIConfiguration configuration, ObjectMapper objectMapper) {
		super(openDistroConfiguration.startClient(), configuration, objectMapper);
	}

}
