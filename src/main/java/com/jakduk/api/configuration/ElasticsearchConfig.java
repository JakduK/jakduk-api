package com.jakduk.api.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.util.List;

@Configuration
public class ElasticsearchConfig {

    public final JakdukProperties.Elasticsearch elasticsearchProperties;

    private List<String> hostAndPort;

    @Autowired
    public ElasticsearchConfig(JakdukProperties.Elasticsearch elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

//    @Bean
//    public RestHighLevelClient client() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(elasticsearchProperties.getHostAndPort.stream().toArray(String[]::new))
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }
}
