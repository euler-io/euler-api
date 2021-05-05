package com.github.euler.api.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.euler.api.model.TemplateList;
import com.github.euler.api.persistence.TemplatesPersistence;

@RestController
public class TemplatesController implements TemplatesApi {

	private final TemplatesPersistence persistence;

	@Autowired
	public TemplatesController(TemplatesPersistence persistence) {
		super();
		this.persistence = persistence;
	}

	@Override
	public ResponseEntity<TemplateList> listTemplates(Integer page, Integer size, @Valid String name) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 10;
		}
		size = Math.min(size, 1000);
		TemplateList list;
		try {
			list = persistence.list(page, size, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new ResponseEntity<TemplateList>(list, HttpStatus.OK);
	}

}
