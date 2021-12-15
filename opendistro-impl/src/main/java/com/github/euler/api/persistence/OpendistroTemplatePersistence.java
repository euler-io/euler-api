package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.api.model.TemplateDetails;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public abstract class OpendistroTemplatePersistence extends AbstractTemplatePersistence implements TemplatePersistence {

    public OpendistroTemplatePersistence(OpenDistroClientManager clientManager, APIConfiguration configuration) {
        super(clientManager, configuration);
    }

    @Override
    public TemplateDetails create(TemplateDetails template) throws IOException {
        Config config = (Config) template.getConfig();
        IndexRequest req = new IndexRequest(getTemplateIndex());
        req.id(template.getName());
        req.source(Map.of(
                "name", template.getName(),
                "config", config.root().render(ConfigRenderOptions.concise())));
        getClient().index(req, getRequestOptions());
        return template;
    }

    @Override
    public void delete(String name) throws IOException {
        DeleteRequest req = new DeleteRequest(getTemplateIndex(), name);
        getClient().delete(req, getRequestOptions());
    }

    @Override
    public TemplateDetails get(String name) throws IOException {
        GetRequest req = new GetRequest(getTemplateIndex(), name);
        GetResponse resp = getClient().get(req, getRequestOptions());
        Map<String, Object> source = resp.getSource();
        if (source != null) {
            return readValue(source);
        } else {
            return null;
        }
    }

    private TemplateDetails readValue(Map<String, Object> source) {
        TemplateDetails templateDetails = new TemplateDetails();

        templateDetails.setName((String) source.get("name"));

        String configStr = (String) source.get("config");
        Config config = ConfigFactory.parseString(configStr);
        templateDetails.setConfig(config);

        return templateDetails;
    }

    RequestOptions getRequestOptions() {
        return RequestOptions.DEFAULT;
    }

}
