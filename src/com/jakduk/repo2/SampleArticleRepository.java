package com.jakduk.repo2;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SampleArticleRepository extends ElasticsearchRepository<Article,String> {
}
