package com.github.euler.api;

import java.net.URI;

public class JobFinished implements APICommand {

    public final String id;
    public final URI uri;

    public JobFinished(String id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

}
