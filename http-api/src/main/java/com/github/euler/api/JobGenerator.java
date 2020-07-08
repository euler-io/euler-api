package com.github.euler.api;

import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class JobGenerator {

    private final ObjectWriter writer;
    private final ObjectReader reader;

    public JobGenerator() {
        super();
        ObjectMapper mapper = new ObjectMapper();
        writer = mapper.writerFor(Map.class);
        reader = mapper.readerFor(Map.class);
    }

    public JobDetails generate(TemplateDetails template, TemplateParams params) {
        Config config = generateConfig(template, params);
        JobDetails details = new JobDetails();
        details.setConfig(toObject(config));
        details.setCreationDate(OffsetDateTime.now());
        details.setStatus(JobStatus.NEW);
        details.setSeed(params.getSeed());
        return details;
    }

    public Config generateConfig(TemplateDetails template, TemplateParams params) {
        Config templateConfig = ConfigFactory.parseString(template.getConfig());
        Config paramsConfig = ConfigFactory.parseString(toJson(params.getParams()));
        paramsConfig = ConfigFactory.empty().withValue("params", paramsConfig.root());
        return templateConfig.resolveWith(paramsConfig);
    }

    private Object toObject(Config config) {
        String json = config.root().render(ConfigRenderOptions.concise());
        try {
            return reader.readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJson(Object config) {
        try {
            return writer.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
