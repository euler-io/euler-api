package com.github.euler.opendistro;

import org.apache.http.client.methods.HttpPost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Validatable;

public class AuthInfoRequest implements Validatable {

    public static final AuthInfoRequest INSTANCE = new AuthInfoRequest();

    private AuthInfoRequest() {
    }

    public Request getRequest() {
        return new Request(HttpPost.METHOD_NAME, "/_opendistro/_security/authinfo");
    }

}
