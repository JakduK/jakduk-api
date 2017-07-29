package com.jakduk.api.common.rabbitmq;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsBoard;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.elasticsearch.EsGallery;
import com.jakduk.api.model.elasticsearch.EsSearchWord;
import com.jakduk.api.model.embedded.BoardItem;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.rabbitmq.EmailPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Slf4j
@Component
public class RabbitMQPublisher {

    private final String QUEUE_EMAIL = "email";
    private final String QUEUE_ELASTICSEARCH = "elasticsearch";

    @Resource private JakdukProperties.Rabbitmq rabbitmqProperties;

    @Autowired private RabbitTemplate rabbitTemplate;

    public void sendWelcome(Locale locale, String recipientEmail, String userName) {
        EmailPayload emailPayload = EmailPayload.builder()
                .locale(locale)
                .type(JakdukConst.EMAIL_TYPE.WELCOME)
                .recipientEmail(recipientEmail)
                .subject("K리그 작두왕에 오신것을 환영합니다.")
                .body(
                        new HashMap<String, String>() {
                            {
                                put("username", userName);
                            }
                        }
                )
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(EmailRoutingKey.EMAIL_WELCOME.getRoutingKey());
        this.publishEmail(routingKey, emailPayload);
    }

    public void sendResetPassword(Locale locale, String recipientEmail, String host) {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(locale)
                .type(JakdukConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail(recipientEmail)
                .subject("jakduk.com-" + JakdukUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
                .extra(
                        new HashMap<String, String>() {
                            {
                                put("host", host);
                            }
                        }
                )
                .body(
                        new HashMap<String, String>() {
                            {
                                put("email", recipientEmail);
                            }
                        }
                )
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(EmailRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());
        this.publishEmail(routingKey, emailPayload);
    }

    public void indexDocumentBoard(String id, Integer seq, CommonWriter writer, String subject, String content, String category,
                                   List<String> galleryIds) {

        EsBoard esBoard = EsBoard.builder()
                .id(id)
                .seq(seq)
                .writer(writer)
                .subject(JakdukUtils.stripHtmlTag(subject))
                .content(JakdukUtils.stripHtmlTag(content))
                .category(category)
                .galleries(galleryIds)
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_BOARD.getRoutingKey());
        this.publishElasticsearch(routingKey, esBoard);
    }

    public void deleteDocumentBoard(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_BOARD.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentComment(String id, BoardItem boardItem, CommonWriter writer, String content, List<String> galleryIds) {

        EsComment esComment = EsComment.builder()
                .id(id)
                .boardItem(boardItem)
                .writer(writer)
                .content(JakdukUtils.stripHtmlTag(content))
                .galleries(galleryIds)
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_COMMENT.getRoutingKey());
        this.publishElasticsearch(routingKey, esComment);
    }

    public void deleteDocumentComment(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_COMMENT.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentGallery(String id, CommonWriter writer, String name) {
        EsGallery esGallery = EsGallery.builder()
                .id(id)
                .writer(writer)
                .name(name)
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_GALLERY.getRoutingKey());
        this.publishElasticsearch(routingKey, esGallery);
    }

    public void deleteDocumentGallery(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_GALLERY.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentSearchWord(String word, CommonWriter writer) {
        EsSearchWord esSearchWord = EsSearchWord.builder()
                .word(word)
                .writer(writer)
                .registerDate(LocalDateTime.now())
                .build();

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_SEARCH_WORD.getRoutingKey());
        this.publishElasticsearch(routingKey, esSearchWord);
    }

    public void publishEmail(String routingKey, EmailPayload message) {
        if (rabbitmqProperties.getQueues().get(QUEUE_EMAIL).getEnabled()) {
            rabbitTemplate.convertAndSend(rabbitmqProperties.getExchangeName(), routingKey, message);
        } else {
            log.info("Can not publish message. {} queue is disabled.", QUEUE_EMAIL);
        }
    }

    public void publishElasticsearch(String routingKey, Object message) {
        if (rabbitmqProperties.getQueues().get(QUEUE_ELASTICSEARCH).getEnabled()) {
            rabbitTemplate.convertAndSend(rabbitmqProperties.getExchangeName(), routingKey, message);
        } else {
            log.info("Can not publish message. {} queue is disabled.", QUEUE_ELASTICSEARCH);
        }
    }

}
