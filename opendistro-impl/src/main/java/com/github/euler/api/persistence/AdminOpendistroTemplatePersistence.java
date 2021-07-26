package com.github.euler.api.persistence;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.opendistro.OpenDistroClient;
import com.typesafe.config.ConfigRenderOptions;

@Service
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class AdminOpendistroTemplatePersistence extends OpendistroTemplatePersistence implements AdminTemplatePersistence {

    @Autowired
    public AdminOpendistroTemplatePersistence(OpenDistroClientManager clientManager, APIConfiguration configuration) {
        super(clientManager, configuration);
    }

    @PostConstruct
    protected void initializeJobIndex() throws IOException {
        boolean autoInitialize = configuration.getConfig().getBoolean("euler.http-api.elasticsearch.auto-initialize-indices");
        if (autoInitialize) {
            String jsonMapping = configuration.getConfig().getConfig("euler.http-api.elasticsearch.template-index.mappings").root().render(ConfigRenderOptions.concise());
            initializeIndex(getClient(), getTemplateIndex(), jsonMapping, getRequestOptions());
        }
    }

    @Override
    protected OpenDistroClient getClient() {
        return clientManager.getAdminClient();
    }

}
