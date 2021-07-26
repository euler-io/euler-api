package com.github.euler.api.persistence;

import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.opendistro.OpenDistroClient;

public abstract class OpendistroPersistence extends BaseOpendistroPersistence {

    protected final OpenDistroClientManager clientManager;

    public OpendistroPersistence(OpenDistroClientManager clientManager) {
        super();
        this.clientManager = clientManager;
    }

    protected abstract OpenDistroClient getClient();

}
