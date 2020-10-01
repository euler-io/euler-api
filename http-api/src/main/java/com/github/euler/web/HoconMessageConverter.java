package com.github.euler.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class HoconMessageConverter extends MappingJackson2HttpMessageConverter {

    public HoconMessageConverter() {
        super();
        setSupportedMediaType();
    }

    public HoconMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        setSupportedMediaType();
    }

    private void setSupportedMediaType() {
        setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("application/hocon")));
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.read(type, contextClass, new ConfigInputMessage(inputMessage, "utf-8"));
    }

    private static class ConfigInputMessage implements HttpInputMessage {

        private final HttpInputMessage original;
        private final String charset;
        private final String parsed;

        public ConfigInputMessage(HttpInputMessage original, String charset) {
            super();
            this.original = original;
            this.charset = charset;
            this.parsed = parse();
        }

        private String parse() {
            try {
                Config config = ConfigFactory.parseReader(new InputStreamReader(original.getBody(), charset));
                return config.root().render(ConfigRenderOptions.concise());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public HttpHeaders getHeaders() {
            return original.getHeaders();
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(this.parsed.getBytes("utf-8"));
        }

    }

}
