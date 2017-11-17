package com.jakduk.api.common.rabbitmq;

import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by pyohwanjang on 2017. 7. 3..
 */

@Getter
public enum ElasticsearchRoutingKey  {

    ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE("elasticsearch-index-document-article"),
    ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE("elasticsearch-delete-document-article"),
    ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE_COMMENT("elasticsearch-index-document-article-comment"),
    ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE_COMMENT("elasticsearch-delete-document-article-comment"),
    ELASTICSEARCH_INDEX_DOCUMENT_GALLERY("elasticsearch-index-document-gallery"),
    ELASTICSEARCH_DELETE_DOCUMENT_GALLERY("elasticsearch-delete-document-gallery"),
    ELASTICSEARCH_INDEX_DOCUMENT_SEARCH_WORD("elasticsearch-index-document-search-word");

    private String routingKey;

    ElasticsearchRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    static public ElasticsearchRoutingKey find(String value) {
        return Arrays.stream(ElasticsearchRoutingKey.values())
                .filter(routingKey -> routingKey.routingKey.equals(value))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ServiceError.ILLEGAL_ARGUMENT));
    }

}
