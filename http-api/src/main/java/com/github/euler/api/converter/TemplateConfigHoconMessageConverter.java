package com.github.euler.api.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.github.euler.api.model.TemplateConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.parser.ConfigDocument;
import com.typesafe.config.parser.ConfigDocumentFactory;

public class TemplateConfigHoconMessageConverter extends AbstractGenericHttpMessageConverter<TemplateConfig> {

	public TemplateConfigHoconMessageConverter() {
		super(MediaType.parseMediaType("application/hocon"), MediaType.APPLICATION_JSON);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TemplateConfig read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return readInternal((Class<? extends TemplateConfig>) contextClass, inputMessage);
	}

	@Override
	protected TemplateConfig readInternal(Class<? extends TemplateConfig> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Config config = ConfigFactory.parseReader(new InputStreamReader(inputMessage.getBody(), "utf-8"));
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setName(config.getString("name"));
		templateConfig.config(config.withOnlyPath("config"));
		return templateConfig;
	}

	@Override
	protected void writeInternal(TemplateConfig t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Config config = (Config) t.getConfig();
		ConfigDocument document = ConfigDocumentFactory.parseString("{}").withValueText("name", t.getName())
				.withValue("config", config.getValue("config"));
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputMessage.getBody(), "utf-8"))) {
			out.write(document.render());
		}
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return TemplateConfig.class.equals(clazz);
	}

}
