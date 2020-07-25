package com.github.euler.opendistro;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.Validatable;
import org.elasticsearch.common.CheckedConsumer;
import org.elasticsearch.common.CheckedFunction;
import org.elasticsearch.common.xcontent.NamedXContentRegistry.Entry;
import org.elasticsearch.common.xcontent.XContentParser;

public class OpenDistroClient extends RestHighLevelClient {

    private final AuthInfoClient authInfoClient = new AuthInfoClient(this);

    public OpenDistroClient(RestClient restClient, CheckedConsumer<RestClient, IOException> doClose, List<Entry> namedXContentEntries) {
        super(restClient, doClose, namedXContentEntries);
    }

    public OpenDistroClient(RestClientBuilder restClientBuilder, List<Entry> namedXContentEntries) {
        super(restClientBuilder, namedXContentEntries);
    }

    public OpenDistroClient(RestClientBuilder restClientBuilder) {
        super(restClientBuilder);
    }

    public AuthInfoClient authInfo() {
        return authInfoClient;
    }

    protected final <Req extends Validatable, Resp> Resp _performRequestAndParseEntity(Req request,
            CheckedFunction<Req, Request, IOException> requestConverter,
            RequestOptions options,
            CheckedFunction<XContentParser, Resp, IOException> entityParser,
            Set<Integer> ignores) throws IOException {
        return super.performRequestAndParseEntity(request, requestConverter, options, entityParser, ignores);
    }

}
