package com.github.euler.api.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.model.Job;
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
    public ResponseEntity<List<Job>> listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection) {
        List<Job> list;
        try {
            list = persistence.list(page, size, sortBy, sortDirection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<List<Job>>(list, HttpStatus.OK);
    }

}
