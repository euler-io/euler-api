package com.github.euler.api.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

public class HoconMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public HoconMessageConverter() {
        super(MediaType.parseMediaType("application/hocon"));
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(contextClass, inputMessage);
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Config config = ConfigFactory.parseReader(new InputStreamReader(inputMessage.getBody(), "utf-8"));
        return ConfigBeanFactory.create(config, clazz);
    }

    @Override
    protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new RuntimeException("Not implemented");
    }

}
