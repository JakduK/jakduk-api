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
    ELASTICSEARCH_DELETE_DOCUMENT_BOARD("elasticsearch-delete-document-board");

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
