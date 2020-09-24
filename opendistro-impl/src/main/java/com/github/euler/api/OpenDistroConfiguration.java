package com.github.euler.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.euler.opendistro.OpenDistroClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

@Configuration
public class OpenDistroConfiguration {

    private final APIConfiguration configuration;
    private OpenDistroClient client;

    @Autowired
    public OpenDistroConfiguration(APIConfiguration configuraiton) {
        super();
        this.configuration = configuraiton;
    }

    @PostConstruct
    public void postConstruct() {
        start();
    }

    private void start() {
        String username = getUsername();
        String password = getPassword();
        client = startClient(username, password);
    }

    public OpenDistroClient startClient(String username, String password) {
        HttpHost[] hosts = getElasticsearchHosts();
        RestClientBuilder builder = RestClient.builder(hosts);
        String ca = getCertificateAuthorities();
        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {

            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                if (ca != null && !isAllowInsecure()) {
                    try {
                        SSLContextBuilder custom = SSLContexts.custom();
                        custom.loadTrustMaterial(TrustStoreLoader.loadTrustStore(ca), new TrustSelfSignedStrategy());
                        httpClientBuilder = httpClientBuilder.setSSLContext(custom.build());
                    } catch (GeneralSecurityException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (isAllowInsecure()) {
                    try {
                        SSLContext sc = SSLContext.getInstance("TLS");
                        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                            public boolean verify(String string, SSLSession ssls) {
                                return true;
                            }
                        });
                        httpClientBuilder = httpClientBuilder.setSSLContext(sc);
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (username != null && !username.isEmpty() && password != null) {
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                    httpClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
                return httpClientBuilder;
            }

        });
        return new OpenDistroClient(builder);
    }

    private String getUsername() {
        Config config = configuration.getConfig();
        try {
            return config.getString("euler.http-api.elasticsearch.username");
        } catch (ConfigException.WrongType e) {
            return null;
        }
    }

    private String getPassword() {
        Config config = configuration.getConfig();
        try {
            return config.getString("euler.http-api.elasticsearch.password");
        } catch (ConfigException.WrongType e) {
            return null;
        }
    }

    @Bean
    public OpenDistroClient opendistroClient() {
        return client;
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        stop();
    }

    private void stop() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    private HttpHost[] getElasticsearchHosts() throws ConfigException.Missing {
        Config config = configuration.getConfig();
        try {
            return config.getStringList("euler.http-api.elasticsearch.hosts").stream()
                    .distinct()
                    .map(h -> toHttpHost(h)).toArray(HttpHost[]::new);
        } catch (ConfigException.WrongType e) {
            return new HttpHost[] { toHttpHost(config.getString("euler.http-api.elasticsearch.hosts")) };
        }
    }

    private HttpHost toHttpHost(String uriStr) {
        try {
            URI uri = new URI(uriStr);
            return new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCertificateAuthorities() {
        try {
            Config config = configuration.getConfig();
            return config.getString("euler.http-api.elasticsearch.ssl.certificate-authorities");
        } catch (ConfigException.Missing e) {
            return null;
        }
    }

    private boolean isAllowInsecure() {
        try {
            Config config = configuration.getConfig();
            return config.getBoolean("euler.http-api.elasticsearch.ssl.allow-insecure");
        } catch (ConfigException.Missing e) {
            return true;
        }
    }

}
