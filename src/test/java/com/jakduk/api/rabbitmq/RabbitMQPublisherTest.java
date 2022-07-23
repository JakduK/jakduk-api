package com.jakduk.api.rabbitmq;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.listener.ElasticsearchListenerTests;
import com.jakduk.api.model.elasticsearch.EsArticle;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;

/**
 * Created by pyohwanjang on 2017. 7. 5..
 */
@SpringBootTest
public class RabbitMQPublisherTest {

	@Resource
	private JakdukProperties.Rabbitmq rabbitProperties;

	@Autowired
	private RabbitMQPublisher sut;

	@Disabled
	@Test
	public void publishElasticsearchTest() {

		EsArticle esArticle = new EsArticle();
		esArticle.setId("595bb024290ad3035636f2ba");
		esArticle.setSeq(262);
		esArticle.setWriter(
			new CommonWriter() {{
				setUserId("userId");
				setUsername("testUser");
			}});
		esArticle.setSubject("subject01");
		esArticle.setContent("content01");
		esArticle.setCategory("FREE");
		//        esArticle.setGalleries(Collections.EMPTY_LIST);

		String routingKey = rabbitProperties.getRoutingKeys()
			.get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE.getRoutingKey());

		sut.publishElasticsearch(routingKey, esArticle);
	}

	@Disabled
	@Test
	public void publishElasticsearchWithArticleComment() {
		EsComment esCommentTests = new EsComment();
		esCommentTests.setId("test-comment-id");
		esCommentTests.setArticle(new ArticleItem("test-article-id", 10, Constants.BOARD_TYPE.FREE.name()));
		esCommentTests.setContent("Hello World");
		esCommentTests.setGalleries(new ArrayList<String>() {{
			add("test-gallery-id");
		}});

		String routingKey = rabbitProperties.getRoutingKeys()
			.get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE_COMMENT.getRoutingKey());

		sut.publishElasticsearch(routingKey, esCommentTests);
	}

	@Disabled
	@AfterEach
	public void after() throws InterruptedException {

		String id = "595bb024290ad3035636f2ba";

		String routingKey = rabbitProperties.getRoutingKeys()
			.get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE.getRoutingKey());

		sut.publishElasticsearch(routingKey, id);

		Thread.sleep(1000 * 5);
	}

}
