package com.jakduk.api.rabbitmq;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsBoard;
import com.jakduk.api.model.embedded.CommonWriter;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 7. 5..
 */
public class RabbitMQPublisherTest extends ApiApplicationTests {

    @Resource
    private JakdukProperties jakdukProperties;

    @Autowired
    private RabbitMQPublisher sut;

    @Test
    public void publishElasticsearchTest() throws InterruptedException {

        EsBoard esBoard = EsBoard.builder()
                .id("595bb024290ad3035636f2ba")
                .seq(262)
                .writer(CommonWriter.builder().userId("userId").username("testUser").build())
                .subject("subject01")
                .content("content01")
                .category("FREE")
//                .galleries(Collections.EMPTY_LIST)
                .build();

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_BOARD.getRoutingKey());

        sut.publishElasticsearch(routingKey, esBoard);
    }

    @After
    public void after() throws InterruptedException {

        String id = "595bb024290ad3035636f2ba";

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_BOARD.getRoutingKey());

        sut.publishElasticsearch(routingKey, id);

        Thread.sleep(1000 * 5);
    }

}
