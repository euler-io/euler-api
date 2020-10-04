package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.opendistro.OpenDistroClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

@Service
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class ESTemplatePersistence extends AbstractTemplatePersistence implements TemplatePersistence {

    @Autowired
    public ESTemplatePersistence(OpenDistroClient client, APIConfiguration configuration) {
        super(client, configuration);
    }

    @PostConstruct
    protected void initializeJobIndex() throws IOException {
        boolean autoInitialize = configuration.getConfig().getBoolean("euler.http-api.elasticsearch.auto-initialize-indices");
        if (autoInitialize) {
            String jsonMapping = configuration.getConfig().getConfig("euler.http-api.elasticsearch.template-index.mappings").root().render(ConfigRenderOptions.concise());
            initializeIndex(getTemplateIndex(), jsonMapping, RequestOptions.DEFAULT);
        }
    }

    @Override
    public TemplateDetails create(TemplateDetails template) throws IOException {
        Config config = (Config) template.getConfig();
        IndexRequest req = new IndexRequest(getTemplateIndex());
        req.id(template.getName());
        req.source(Map.of("name", template.getName(),
                "config", config.root().render(ConfigRenderOptions.concise())));
        client.index(req, RequestOptions.DEFAULT);
        return template;
    }

    @Override
    public void delete(String name) throws IOException {
        DeleteRequest req = new DeleteRequest(getTemplateIndex(), name);
        client.delete(req, RequestOptions.DEFAULT);
    }

    @Override
    public TemplateDetails get(String name) throws IOException {
        GetRequest req = new GetRequest(getTemplateIndex(), name);
        GetResponse resp = client.get(req, RequestOptions.DEFAULT);
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

}
