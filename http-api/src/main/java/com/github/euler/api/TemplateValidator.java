package com.github.euler.api;

import java.util.regex.Pattern;

import com.github.euler.api.model.TemplateConfig;

public class TemplateValidator {

	private final Pattern validNamePattern = Pattern.compile("[a-z0-9\\-_]+");

	public boolean isValid(TemplateConfig config) {
		return isNameValid(config.getName());
	}

	public boolean isNameValid(String name) {
		return name != null && !name.isBlank() && validNamePattern.matcher(name).matches();
	}

}
