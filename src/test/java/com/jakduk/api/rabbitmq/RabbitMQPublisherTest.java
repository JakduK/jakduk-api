package com.jakduk.api.rabbitmq;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsArticle;
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
    public void publishElasticsearchTest() {

        EsArticle esArticle = new EsArticle();
        esArticle.setId("595bb024290ad3035636f2ba");
        esArticle.setSeq(262);
        esArticle.setWriter(
                new CommonWriter(){{
                    setUserId("userId");
                    setUsername("testUser");
        }});
        esArticle.setSubject("subject01");
        esArticle.setContent("content01");
        esArticle.setCategory("FREE");
//        esArticle.setGalleries(Collections.EMPTY_LIST);

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE.getRoutingKey());

        sut.publishElasticsearch(routingKey, esArticle);
    }

    @After
    public void after() throws InterruptedException {

        String id = "595bb024290ad3035636f2ba";

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE.getRoutingKey());

        sut.publishElasticsearch(routingKey, id);

        Thread.sleep(1000 * 5);
    }

}
