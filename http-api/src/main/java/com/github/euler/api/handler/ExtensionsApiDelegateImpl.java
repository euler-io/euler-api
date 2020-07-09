package com.github.euler.api.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.model.ContextConverter;
import com.github.euler.api.model.Extension;
import com.github.euler.api.model.TaskConverter;
import com.github.euler.api.model.TypeConverter;
import com.github.euler.configuration.ContextConfigConverter;
import com.github.euler.configuration.EulerConfigConverter;
import com.github.euler.configuration.EulerExtension;
import com.github.euler.configuration.TaskConfigConverter;
import com.github.euler.configuration.TypeConfigConverter;

@Service
public class ExtensionsApiDelegateImpl implements ExtensionsApiDelegate {

    private EulerConfigConverter converter = new EulerConfigConverter();

    @Override
    public ResponseEntity<List<Extension>> getExtensions() {
        List<Extension> extensions = converter.getExtensions().stream()
                .map(e -> convert(e)).collect(Collectors.toList());
        return new ResponseEntity<List<Extension>>(extensions, HttpStatus.OK);
    }

    private Extension convert(EulerExtension e) {
        return new Extension()
                .pathConverters(e.pathConverters().stream()
                        .map(p -> convert(p)).collect(Collectors.toList()))
                .typeConverters(e.typeConverters().stream()
                        .map(t -> convert(t)).collect(Collectors.toList()))
                .taskConverters(e.taskConverters().stream()
                        .map(t -> convert(t)).collect(Collectors.toList()))
                .description(e.getDescription());
    }

    private ContextConverter convert(ContextConfigConverter p) {
        return new ContextConverter()
                .description(p.getDescription())
                .path(p.path());
    }

    private TypeConverter convert(TypeConfigConverter<?> t) {
        return new TypeConverter()
                .description(t.getDescription())
                .type(t.type())
                .configType(t.configType());
    }

    private TaskConverter convert(TaskConfigConverter t) {
        return new TaskConverter()
                .description(t.getDescription())
                .type(t.type());
    }

}
