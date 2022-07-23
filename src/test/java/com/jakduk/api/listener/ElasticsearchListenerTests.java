package com.jakduk.api.listener;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.service.SearchService;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringJUnitConfig(ElasticsearchListenerTests.Config.class)
@SpringRabbitTest
@TestPropertySource(properties = "jakduk.rabbitmq.queues.elasticsearch.binding-queue-name = " + ElasticsearchListenerTests.ROUTING_KEY)
public class ElasticsearchListenerTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@MockBean
	private JakdukProperties.Rabbitmq rabbitmqProperties;

	@MockBean
	private SearchService searchService;

	public final static String ROUTING_KEY = "dev.elasticsearch.index-document-article-comment";

	@Test
	public void indexDocumentBoardComment() throws IOException {
		EsComment esComment = new EsComment();
		esComment.setId("test-comment-id");
		esComment.setArticle(new ArticleItem("test-article-id", 10, Constants.BOARD_TYPE.FREE.name()));
		esComment.setContent("Hello World");
		esComment.setGalleries(new ArrayList<String>() {{
			add("test-gallery-id");
		}});

		when(rabbitmqProperties.getRoutingKeys())
			.thenReturn(new HashMap<String, String>() {{
				put("elasticsearch-index-document-article-comment", ElasticsearchListenerTests.ROUTING_KEY);
			}});

		rabbitTemplate.convertAndSend(ElasticsearchListenerTests.ROUTING_KEY, esComment, message -> {
			message.getMessageProperties().getHeaders().put(AmqpHeaders.RECEIVED_ROUTING_KEY, ElasticsearchListenerTests.ROUTING_KEY);
			message.getMessageProperties().getHeaders().put("__TypeId__", "com.jakduk.api.model.notexists.elasticsearch"); // 일부러 존재 하지 않는 packace 를 넣는다.
			return message;
		});

		verify(searchService, times(1)).indexDocumentBoardComment(eq(esComment));
	}

	@TestConfiguration
	public static class Config {

		@Bean
		public TestRabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
			TestRabbitTemplate rabbitTemplate = new TestRabbitTemplate(connectionFactory);
			rabbitTemplate.setMessageConverter(messageConverter);
			return rabbitTemplate;
		}

		@Bean
		public ConnectionFactory connectionFactory() {
			ConnectionFactory factory = mock(ConnectionFactory.class);
			Connection connection = mock(Connection.class);
			Channel channel = mock(Channel.class);
			willReturn(connection).given(factory).createConnection();
			willReturn(channel).given(connection).createChannel(anyBoolean());
			given(channel.isOpen()).willReturn(true);
			return factory;
		}

		@Bean
		public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
			SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
			factory.setConnectionFactory(connectionFactory);
			return factory;
		}

		@Bean
		public MessageConverter messageConverter() {
			return new Jackson2JsonMessageConverter(ObjectMapperUtils.getObjectMapper());
		}

		@Bean
		public ElasticsearchListener listener() {
			return new ElasticsearchListener();
		}
	}
}
