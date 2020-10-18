package com.github.euler.api;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.persistence.TemplatePersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;

@Configuration
public class TemplatesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplatesConfig.class);

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
            LOGGER.info("Loading default templates from {}.", templatesUrl);
            Config templatesConfig = ConfigFactory.parseURL(new URL(templatesUrl));
            ConfigList templatesList = templatesConfig.getList("templates");
            for (ConfigValue templateConfig : templatesList) {
                loadTemplate(((ConfigObject) templateConfig).toConfig());
            }
        } else {
            LOGGER.info("Default templates not found.");
        }
    }

    private void loadTemplate(Config config) throws IOException {
        String name = config.getString("name");
        TemplateDetails details = templatePersistence.get(name);
        if (details == null) {
            LOGGER.info("Creating index {}.", name);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Template {}", name);
                LOGGER.debug("{}", config.root().render(ConfigRenderOptions.defaults()));
            }
            details = new TemplateDetails();
            details.setConfig(config.withOnlyPath("config"));
            details.setName(name);
            templatePersistence.create(details);
        } else {
            LOGGER.info("Template {} already exists.", name);
        }
    }

}
