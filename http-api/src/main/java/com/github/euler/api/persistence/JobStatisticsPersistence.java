package com.github.euler.api.persistence;

import java.util.List;

import com.github.euler.api.model.JobStatistics;

public interface JobStatisticsPersistence {

    List<JobStatistics> list(String jobId);

}
