package com.github.euler.api.persistence;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;

public abstract class AbstractTemplatePersistence extends OpendistroPersistence {

    protected final APIConfiguration configuration;

    public AbstractTemplatePersistence(OpenDistroClientManager clientManager, APIConfiguration configuration) {
        super(clientManager);
        this.configuration = configuration;
    }

    protected String getTemplateIndex() {
        return configuration.getConfig().getString("euler.http-api.elasticsearch.template-index.name");
    }

}
