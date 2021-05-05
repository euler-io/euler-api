package com.github.euler.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.github.euler.api.controller.AuthController;
import com.github.euler.api.persistence.OpendistroJobDetailsPersistence;
import com.github.euler.api.security.SecurityConfiguration;

@SpringBootApplication
@ComponentScan(basePackageClasses = { MappingConfiguration.class, OpendistroJobDetailsPersistence.class,
		SecurityConfiguration.class, AuthController.class, EulerApiCustomiser.class })
public class OpenDistroEulerHttpApiApplication extends EulerHttpApiApplication {

	public static void main(String[] args) {
		OpenDistroEulerHttpApiApplication api = new OpenDistroEulerHttpApiApplication();
		api.start(args);
	}

}
