package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateList;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class ESTemplatesPersistence extends AbstractTemplatePersistence implements TemplatesPersistence {

    private final ObjectMapper objectMapper;

    @Autowired
    public ESTemplatesPersistence(OpenDistroClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration);
        this.objectMapper = objectMapper;
    }

    @Override
    public TemplateList list(Integer page, Integer size, String name) throws IOException {
        SearchRequest req = new SearchRequest(getTemplateIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder query = QueryBuilders.boolQuery()
                .should(QueryBuilders.idsQuery().addIds(name))
                .should(QueryBuilders.matchQuery("name", name))
                .minimumShouldMatch(1);
        searchSourceBuilder.query(query);
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(page * size);

        SearchResponse response = client.search(req, RequestOptions.DEFAULT);
        int total = Long.valueOf(response.getHits().getTotalHits().value).intValue();
        List<Template> templates = Arrays.stream(response.getHits().getHits())
                .map(h -> convert(h))
                .collect(Collectors.toList());

        TemplateList list = new TemplateList();
        list.setTotal(total);
        list.setTemplates(templates);
        return list;
    }

    protected Template convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        return objectMapper.convertValue(source, Template.class);
    }

}
