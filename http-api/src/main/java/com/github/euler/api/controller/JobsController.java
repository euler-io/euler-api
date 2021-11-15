package com.github.euler.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.euler.api.model.JobList;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.api.persistence.UserJobPersistence;

@RestController
public class JobsController implements JobsAPI {

    private final UserJobPersistence persistence;

    @Autowired
    public JobsController(UserJobPersistence persistence) {
        super();
        this.persistence = persistence;
    }

    @Override
    public ResponseEntity<JobList> listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection,
            JobStatus[] status, String[] tags) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        size = Math.min(size, 1000);
        if (sortBy == null) {
            sortBy = SortBy.CREATION_DATE;
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.ASC;
        }
        JobList list;
        try {
            list = persistence.list(page, size, sortBy, sortDirection, status, tags);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<JobList>(list, HttpStatus.OK);
    }

}
