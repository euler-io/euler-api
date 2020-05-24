package com.github.euler.api.persistence;

import com.github.euler.api.model.JobDetails;

public interface JobDetailsPersistence {

    public JobDetails get(String id);

}
