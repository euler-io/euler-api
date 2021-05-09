package com.github.euler.api;

import static com.github.euler.api.EulerBanner.getVersion;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class EulerApiCustomiser {

//	@Bean
	public OpenApiCustomiser customiser() {
		return openApi -> {
			openApi.getInfo().setVersion(getVersion());
		};
	}

}
