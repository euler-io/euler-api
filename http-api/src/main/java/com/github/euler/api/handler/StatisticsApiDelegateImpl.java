package com.github.euler.api.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.model.JobStatistics;
import com.github.euler.api.persistence.JobStatisticsPersistence;

@Service
public class StatisticsApiDelegateImpl implements StatisticsApiDelegate {

    private final JobStatisticsPersistence persistence;

    @Autowired
    public StatisticsApiDelegateImpl(JobStatisticsPersistence persistence) {
        super();
        this.persistence = persistence;
    }

    @Override
    public ResponseEntity<List<JobStatistics>> getJobsStatistics(String jobId) {
        List<JobStatistics> list = persistence.list(jobId);
        return new ResponseEntity<List<JobStatistics>>(list, HttpStatus.OK);
    }

}
