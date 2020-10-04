package com.github.euler.api;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class JobGeneratorTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testResolveSubstitutions() throws Exception {
        String templateConfigRaw = "{module: { config1: ${params.param1}}}";
        String paramsRaw = "{ param1: value1}";

        Config templateConfig = ConfigFactory.parseString(templateConfigRaw);
        Config paramsConfig = ConfigFactory.parseString(paramsRaw);

        TemplateDetails template = new TemplateDetails();
        template.setConfig(templateConfig);

        TemplateParams params = new TemplateParams();
        params.setParams(toObject(paramsConfig));

        JobGenerator generator = new JobGenerator();
        Config config = generator.generateConfig(template, params);

        assertEquals("value1", config.getString("module.config1"));
    }

    private Object toObject(Config config) throws JsonMappingException, JsonProcessingException {
        String render = config.root().render(ConfigRenderOptions.concise());
        Object obj = mapper.readerFor(Map.class).readValue(render);
        return obj;
    }

}
