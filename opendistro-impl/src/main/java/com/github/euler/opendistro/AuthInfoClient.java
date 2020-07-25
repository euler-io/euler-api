package com.github.euler.opendistro;

import static java.util.Collections.emptySet;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;

public class AuthInfoClient {

    private OpenDistroClient client;

    public AuthInfoClient(OpenDistroClient client) {
        this.client = client;
    }

    public OpenDistroAuthResponse authInfo(RequestOptions options) throws IOException {
        return client._performRequestAndParseEntity(AuthInfoRequest.INSTANCE, AuthInfoRequest::getRequest, options,
                OpenDistroAuthResponse::fromXContent, emptySet());
    }

}
