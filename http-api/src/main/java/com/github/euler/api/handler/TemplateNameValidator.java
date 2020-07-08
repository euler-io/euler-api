package com.github.euler.api.handler;

import java.util.regex.Pattern;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

public class TemplateNameValidator {

    private final Pattern validNamePattern = Pattern.compile("[a-z0-9\\-_]+");

    public boolean isValid(String name) {
        return name != null && !name.isBlank() && validNamePattern.matcher(name).matches();
    }

    public boolean isConfigValid(String config) {
        try {
            ConfigFactory.parseString(config);
            return true;
        } catch (ConfigException.Parse e) {
            return false;
        }
    }

}
