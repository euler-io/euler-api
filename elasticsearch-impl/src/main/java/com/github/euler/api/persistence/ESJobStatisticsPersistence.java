package com.github.euler.api.persistence;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.euler.api.model.JobStatistics;

@Service
public class ESJobStatisticsPersistence implements JobStatisticsPersistence {

    @Override
    public List<JobStatistics> list(String jobId) {
        // TODO Auto-generated method stub
        return null;
    }

}
