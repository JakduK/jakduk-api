package com.jakduk.core.common;

import com.jakduk.core.common.rabbitmq.ElasticsearchRoutingKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by pyohwanjang on 2017. 7. 3..
 */
public class RabbitMQRoutingKeyTest {

    @Test
    public void rabbitMqRoutingKeyTest() {
        ElasticsearchRoutingKey elasticsearchRoutingKey = ElasticsearchRoutingKey.find("elasticsearch-index-document-board");

        Assert.assertTrue(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_BOARD.name().equals(elasticsearchRoutingKey.name()));
    }
}
