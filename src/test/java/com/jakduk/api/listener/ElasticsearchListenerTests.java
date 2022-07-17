package com.jakduk.api.listener;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.service.SearchService;
import com.rabbitmq.client.Channel;

import jdk.nashorn.internal.ir.annotations.Ignore;

@Ignore
@SpringJUnitConfig(ElasticsearchListenerTests.Config.class)
@SpringRabbitTest
@TestPropertySource(properties = "jakduk.rabbitmq.queues.elasticsearch.binding-queue-name = dev.elasticsearch.index-document-article-comment")
@MockBeans({
	@MockBean(JakdukProperties.Rabbitmq.class), @MockBean(SearchService.class)
})
public class ElasticsearchListenerTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void test() {
		EsComment esComment = new EsComment();
		esComment.setId("test-comment-id");
		esComment.setArticle(new ArticleItem("test-article-id", 10, Constants.BOARD_TYPE.FREE.name()));
		esComment.setContent("Hello World");
		esComment.setGalleries(new ArrayList<String>() {{
			add("test-gallery-id");
		}});

		rabbitTemplate.convertAndSend("dev.elasticsearch.index-document-article-comment", esComment);
	}

	@TestConfiguration
	public static class Config {

		@Bean
		public TestRabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
			TestRabbitTemplate rabbitTemplate = new TestRabbitTemplate(connectionFactory);
			rabbitTemplate.setMessageConverter(this.messageConverter());
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

/*		@Bean
		public ConnectionFactory connectionFactory() {
			CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
			connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
			connectionFactory.setPublisherReturns(true);
			return connectionFactory;
		}*/


		@Bean
		public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
			SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
			factory.setConnectionFactory(connectionFactory);
			// factory.setMessageConverter(messageConverter());
			return factory;
		}

		@Bean
		public Queue queue() {
			return new Queue("dev.elasticsearch");
		}

		@Bean
		public TopicExchange topicExchange() {
			return new TopicExchange("jakduk-dev");
		}

		@Bean
		public List<Binding> binding(TopicExchange exchange, List<Queue> queues) {
			return queues.stream()
				.map(queue -> BindingBuilder.bind(queue).to(exchange).with("dev.elasticsearch.*"))
				.collect(Collectors.toList());
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
