package com.github.euler.api;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OffsetDateTimeIO {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private OffsetDateTimeIO() {
        super();
    }

    public static class Serializer extends JsonSerializer<OffsetDateTime> {

        private final DateTimeFormatter formatter;

        public Serializer() {
            super();
            formatter = DateTimeFormatter.ofPattern(FORMAT);
        }

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(serialize(value));
        }

        public String serialize(OffsetDateTime value) {
            return value.format(formatter);
        }
    }

    public static class Deserializer extends JsonDeserializer<OffsetDateTime> {

        private final DateTimeFormatter formatter;

        public Deserializer() {
            super();
            formatter = DateTimeFormatter.ofPattern(FORMAT);
        }

        @Override
        public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            CharSequence str = p.getText();
            return deserialize(str);
        }

        public OffsetDateTime deserialize(CharSequence str) {
            return OffsetDateTime.parse(str, formatter);
        }

    }

}
