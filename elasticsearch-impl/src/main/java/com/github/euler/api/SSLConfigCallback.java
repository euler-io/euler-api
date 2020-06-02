package com.github.euler.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClientBuilder;

final class SSLConfigCallback implements RestClientBuilder.HttpClientConfigCallback {

    private final String ca;

    public SSLConfigCallback(String ca) {
        super();
        this.ca = ca;
    }

    @Override
    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
        try {
            SSLContextBuilder custom = SSLContexts.custom();
            custom.loadTrustMaterial(TrustStoreLoader.loadTrustStore(ca), new TrustSelfSignedStrategy());
            httpClientBuilder = httpClientBuilder.setSSLContext(custom.build());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return httpClientBuilder;
    }
}