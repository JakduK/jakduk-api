package com.jakduk.api.listener;

import com.jakduk.api.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.elasticsearch.EsArticle;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.elasticsearch.EsGallery;
import com.jakduk.api.model.elasticsearch.EsSearchWord;
import com.jakduk.api.service.SearchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 6. 20..
 */

@Component
public class ElasticsearchListener {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private JakdukProperties.Rabbitmq rabbitmqProperties;

	@Autowired
	private SearchService searchService;

	@RabbitListener(queues = "${jakduk.rabbitmq.queues.elasticsearch.binding-queue-name}")
	public void receive(Message message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws
		IOException {

		String findKey = rabbitmqProperties.getRoutingKeys().entrySet().stream()
			.filter(entity -> entity.getValue().equals(routingKey))
			.findFirst()
			.map(Map.Entry::getKey)
			.orElseThrow(() -> new ServiceException(ServiceError.ILLEGAL_ARGUMENT));

		ElasticsearchRoutingKey elasticsearchRoutingKey = ElasticsearchRoutingKey.find(findKey);

		switch (elasticsearchRoutingKey) {
			case ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE:
				EsArticle esArticle = ObjectMapperUtils.readValue(message.getBody(), EsArticle.class);
				searchService.indexDocumentArticle(esArticle);
				break;

			case ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE:
				String boardId = ObjectMapperUtils.readValue(message.getBody(), String.class);
				searchService.deleteDocumentBoard(boardId);
				break;

			case ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE_COMMENT:
				EsComment esComment = ObjectMapperUtils.readValue(message.getBody(), EsComment.class);
				searchService.indexDocumentBoardComment(esComment);
				break;

			case ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE_COMMENT:
				String commentId = ObjectMapperUtils.readValue(message.getBody(), String.class);
				searchService.deleteDocumentBoardComment(commentId);
				break;

			case ELASTICSEARCH_INDEX_DOCUMENT_GALLERY:
				EsGallery esGallery = ObjectMapperUtils.readValue(message.getBody(), EsGallery.class);
				searchService.indexDocumentGallery(esGallery);
				break;

			case ELASTICSEARCH_DELETE_DOCUMENT_GALLERY:
				String galleryId = ObjectMapperUtils.readValue(message.getBody(), String.class);
				searchService.deleteDocumentGallery(galleryId);
				break;

			case ELASTICSEARCH_INDEX_DOCUMENT_SEARCH_WORD:
				EsSearchWord esSearchWord = ObjectMapperUtils.readValue(message.getBody(), EsSearchWord.class);
				searchService.indexDocumentSearchWord(esSearchWord);
				break;

		}
	}

}
