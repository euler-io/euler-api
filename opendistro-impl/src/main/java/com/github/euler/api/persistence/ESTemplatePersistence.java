package com.github.euler.api.persistence;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.opendistro.OpenDistroClient;
import com.typesafe.config.ConfigRenderOptions;

@Service
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class ESTemplatePersistence extends AbstractTemplatePersistence implements TemplatePersistence {

    private final ObjectMapper objectMapper;
    private final ObjectWriter writer;
    private final ObjectReader reader;

    @Autowired
    public ESTemplatePersistence(OpenDistroClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration);
        this.objectMapper = objectMapper;
        writer = this.objectMapper.writerFor(TemplateDetails.class);
        reader = this.objectMapper.readerFor(TemplateDetails.class);
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
        IndexRequest req = new IndexRequest(getTemplateIndex());
        req.id(template.getName());
        req.source(toBytes(template), XContentType.JSON);
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
        byte[] source = resp.getSourceAsBytes();
        if (source != null) {
            return readValue(source);
        } else {
            return null;
        }
    }

    private byte[] toBytes(TemplateDetails template) {
        try {
            return writer.writeValueAsBytes(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected TemplateDetails readValue(byte[] sourceAsBytes) throws IOException {
        return reader.readValue(sourceAsBytes);
    }

}
