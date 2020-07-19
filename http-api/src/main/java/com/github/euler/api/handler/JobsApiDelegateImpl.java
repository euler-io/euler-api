package com.github.euler.api.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.model.JobList;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.api.persistence.JobPersistence;

@Service
public class JobsApiDelegateImpl implements JobsApiDelegate {

    private final JobPersistence persistence;

    @Autowired
    public JobsApiDelegateImpl(JobPersistence persistence) {
        super();
        this.persistence = persistence;
    }

    @Override
    public ResponseEntity<JobList> listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection, JobStatus status) {
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
            list = persistence.list(page, size, sortBy, sortDirection, status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<JobList>(list, HttpStatus.OK);
    }

}
