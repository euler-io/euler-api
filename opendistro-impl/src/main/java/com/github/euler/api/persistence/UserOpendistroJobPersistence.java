package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroConfiguration;

@Service
public class UserOpendistroJobPersistence extends OpendistroJobPersistence implements UserJobPersistence {

	@Autowired
	public UserOpendistroJobPersistence(OpenDistroConfiguration openDistroConfiguration, APIConfiguration configuration,
			ObjectMapper objectMapper) {
		super(openDistroConfiguration.startClient(null, null), configuration, objectMapper);
	}

	@Override
	RequestOptions getRequestOptions() {
		return buildOptions();
	}

}
