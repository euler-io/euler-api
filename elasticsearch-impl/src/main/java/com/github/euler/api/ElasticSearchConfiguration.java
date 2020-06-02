package com.github.euler.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

@Configuration
public class ElasticSearchConfiguration {

    private final APIConfiguration configuration;
    private RestHighLevelClient client;

    @Autowired
    public ElasticSearchConfiguration(APIConfiguration configuraiton) {
        super();
        this.configuration = configuraiton;
    }

    @PostConstruct
    public void postConstruct() {
        start();
    }

    private void start() {
        HttpHost[] hosts = getElasticsearchHosts();
        RestClientBuilder builder = RestClient.builder(hosts);
        String ca = getCertificateAuthorities();
        if (ca != null) {
            builder.setHttpClientConfigCallback(new SSLConfigCallback(ca));
        }
        client = new RestHighLevelClient(builder);
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
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
            return new HttpHost[] { toHttpHost(config.getString("elasticsearch.hosts")) };
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

    String getCertificateAuthorities() {
        try {
            Config config = configuration.getConfig();
            return config.getString("euler.http-api.elasticsearch.ssl.certificateAuthorities");
        } catch (ConfigException.Missing e) {
            return null;
        }
    }

}
