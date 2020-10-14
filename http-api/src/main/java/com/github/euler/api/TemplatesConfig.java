package com.github.euler.api;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.persistence.TemplatePersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

@Configuration
public class TemplatesConfig {

    private static final String EULER_HTTP_API_TEMPLATES_URL = "euler.http-api.templates-url";

    private final APIConfiguration config;
    private final TemplatePersistence templatePersistence;

    @Autowired
    public TemplatesConfig(APIConfiguration config, TemplatePersistence templatePersistence) {
        super();
        this.config = config;
        this.templatePersistence = templatePersistence;
    }

    @PostConstruct
    protected void loadDefaultTemplates() throws IOException {
        if (config.getConfig().hasPath(EULER_HTTP_API_TEMPLATES_URL)) {
            String templatesUrl = config.getConfig().getString(EULER_HTTP_API_TEMPLATES_URL);
            Config templatesConfig = ConfigFactory.parseURL(new URL(templatesUrl));
            ConfigList templatesList = templatesConfig.getList("templates");
            for (ConfigValue templateConfig : templatesList) {
                loadTemplate(((ConfigObject) templateConfig).toConfig());
            }
        }
    }

    private void loadTemplate(Config config) throws IOException {
        String name = config.getString("name");
        TemplateDetails details = templatePersistence.get(name);
        if (details == null) {
            details = new TemplateDetails();
            details.setConfig(config.withOnlyPath("config"));
            details.setName(name);
            templatePersistence.create(details);
        }
    }

}
