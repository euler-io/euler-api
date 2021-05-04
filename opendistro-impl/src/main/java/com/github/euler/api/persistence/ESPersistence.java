package com.github.euler.api.persistence;

import com.github.euler.opendistro.OpenDistroClient;

public abstract class ESPersistence extends BaseESPersistence {

    protected final OpenDistroClient client;

    public ESPersistence(OpenDistroClient client) {
        super();
        this.client = client;
    }

}
