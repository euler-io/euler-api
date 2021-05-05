package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroConfiguration;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateList;

@Service
public class OpendistroTemplatesPersistence extends AbstractTemplatePersistence implements TemplatesPersistence {

    private final ObjectMapper objectMapper;

    @Autowired
    public OpendistroTemplatesPersistence(OpenDistroConfiguration openDistroConfiguration, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(openDistroConfiguration.startClient(null, null), configuration);
        this.objectMapper = objectMapper.copy().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TemplateList list(Integer page, Integer size, String name) throws IOException {
        SearchRequest req = new SearchRequest(getTemplateIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .should(QueryBuilders.idsQuery().addIds(name))
                .minimumShouldMatch(1);

        if (name != null && !name.isBlank()) {
            query.should(QueryBuilders.matchQuery("name", name));
        }
        searchSourceBuilder.query(query);
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(page * size);

        SearchResponse response = client.search(req, buildOptions());
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
