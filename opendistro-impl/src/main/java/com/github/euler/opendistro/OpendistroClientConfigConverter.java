package com.github.euler.opendistro;

import org.elasticsearch.client.RestHighLevelClient;

import com.github.euler.configuration.ConfigContext;
import com.github.euler.configuration.TypeConfigConverter;
import com.github.euler.configuration.TypesConfigConverter;
import com.typesafe.config.Config;

public class OpendistroClientConfigConverter implements TypeConfigConverter<RestHighLevelClient> {

    private final OpenDistroClient client;

    public OpendistroClientConfigConverter(OpenDistroClient client) {
        super();
        this.client = client;
    }

    @Override
    public String configType() {
        return "provided";
    }

    @Override
    public String type() {
        return "elasticsearch-client";
    }

    @Override
    public RestHighLevelClient convert(Config config, ConfigContext ctx, TypesConfigConverter typesConfigConverter) {
        return client;
    }

}
