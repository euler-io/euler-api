package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroConfiguration;

@Service
public class UserOpendistroJobDetailsPersistence extends OpendistroJobDetailsPersistence
		implements UserJobDetailsPersistence {

	@Autowired
	public UserOpendistroJobDetailsPersistence(OpenDistroConfiguration openDistroConfiguration,
			APIConfiguration configuration) {
		super(openDistroConfiguration.startClient(null, null), configuration);
	}

	@Override
	RequestOptions getRequestOptions() {
		return buildOptions();
	}

}
