package com.github.euler.api;

import java.time.OffsetDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.euler.web.HoconMessageConverter;
import com.monitorjbl.json.JsonViewModule;

@Configuration
public class MappingConfiguration {

    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("euler");
        module.addSerializer(OffsetDateTime.class, new OffsetDateTimeIO.Serializer());
        module.addDeserializer(OffsetDateTime.class, new OffsetDateTimeIO.Deserializer());
        mapper.registerModule(module);
        mapper.registerModule(new JsonViewModule());
        return mapper;
    }

    @Bean
    public HoconMessageConverter hoconMessageConverter() {
        return new HoconMessageConverter();
    }
}
