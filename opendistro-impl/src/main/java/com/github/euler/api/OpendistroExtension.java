package com.github.euler.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.github.euler.configuration.EulerExtension;
import com.github.euler.configuration.TypeConfigConverter;
import com.github.euler.opendistro.OpenDistroClient;
import com.github.euler.opendistro.OpendistroClientConfigConverter;

@Configuration
public class OpendistroExtension implements EulerExtension {

    private final OpenDistroClient client;

    @Autowired
    public OpendistroExtension(OpenDistroClient client) {
        super();
        this.client = client;
    }

    @Override
    public List<TypeConfigConverter<?>> typeConverters() {
        return Arrays.asList(new OpendistroClientConfigConverter(client));
    }

}
