package com.jakduk.core.common.rabbitmq;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by pyohwanjang on 2017. 7. 3..
 */

@Getter
public enum ElasticsearchRoutingKey  {

    ELASTICSEARCH_INDEX_DOCUMENT_BOARD("elasticsearch-index-document-board"),
    ELASTICSEARCH_DELETE_DOCUMENT_BOARD("elasticsearch-delete-document-board"),
    ELASTICSEARCH_INDEX_DOCUMENT_COMMENT("elasticsearch-index-document-comment"),
    ELASTICSEARCH_DELETE_DOCUMENT_COMMENT("elasticsearch-delete-document-comment"),
    ELASTICSEARCH_INDEX_DOCUMENT_GALLERY("elasticsearch-index-document-gallery"),
    ELASTICSEARCH_DELETE_DOCUMENT_GALLERY("elasticsearch-delete-document-gallery"),
    ELASTICSEARCH_INDEX_DOCUMENT_SEARCH_WORD("elasticsearch-index-document-search-word");

    String routingKey;

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
