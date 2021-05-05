package com.github.euler.api.persistence;

import com.github.euler.api.APIConfiguration;
import com.github.euler.opendistro.OpenDistroClient;

public class AbstractTemplatePersistence extends OpendistroPersistence {

    protected final APIConfiguration configuration;

    public AbstractTemplatePersistence(OpenDistroClient client, APIConfiguration configuration) {
        super(client);
        this.configuration = configuration;
    }

    protected String getTemplateIndex() {
        return configuration.getConfig().getString("euler.http-api.elasticsearch.template-index.name");
    }

}
