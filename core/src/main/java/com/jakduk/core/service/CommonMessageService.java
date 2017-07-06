package com.jakduk.core.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.core.common.rabbitmq.EmailRoutingKey;
import com.jakduk.core.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.model.elasticsearch.EsBoard;
import com.jakduk.core.model.elasticsearch.EsComment;
import com.jakduk.core.model.elasticsearch.EsGallery;
import com.jakduk.core.model.elasticsearch.EsSearchWord;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.rabbitmq.EmailPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jang,Pyohwan on 2017. 7. 5..
 */

@Service
public class CommonMessageService {

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    public void sendWelcome(Locale locale, String recipientEmail, String userName) {
        EmailPayload emailPayload = EmailPayload.builder()
                .locale(locale)
                .type(CoreConst.EMAIL_TYPE.WELCOME)
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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_WELCOME.getRoutingKey());
        rabbitMQPublisher.publishEmail(routingKey, emailPayload);
    }

    public void sendResetPassword(Locale locale, String recipientEmail, String host) {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(locale)
                .type(CoreConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail(recipientEmail)
                .subject("jakduk.com-" + CoreUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());
        rabbitMQPublisher.publishEmail(routingKey, emailPayload);
    }

    public void indexDocumentBoard(String id, Integer seq, CommonWriter writer, String subject, String content, String category,
                                   List<String> galleryIds) {

        EsBoard esBoard = EsBoard.builder()
                .id(id)
                .seq(seq)
                .writer(writer)
                .subject(CoreUtils.stripHtmlTag(subject))
                .content(CoreUtils.stripHtmlTag(content))
                .category(category)
                .galleries(galleryIds)
                .build();

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_BOARD.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, esBoard);
    }

    public void deleteDocumentBoard(String id) {
        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_BOARD.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentComment(String id, BoardItem boardItem, CommonWriter writer, String content, List<String> galleryIds) {

        EsComment esComment = EsComment.builder()
                .id(id)
                .boardItem(boardItem)
                .writer(writer)
                .content(CoreUtils.stripHtmlTag(content))
                .galleries(galleryIds)
                .build();

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_COMMENT.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, esComment);
    }

    public void deleteDocumentComment(String id) {
        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_COMMENT.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentGallery(String id, CommonWriter writer, String name) {
        EsGallery esGallery = EsGallery.builder()
                .id(id)
                .writer(writer)
                .name(name)
                .build();

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_GALLERY.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, esGallery);
    }

    public void deleteDocumentGallery(String id) {
        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_GALLERY.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentSearchWord(String word, CommonWriter writer) {
        EsSearchWord esSearchWord = EsSearchWord.builder()
                .word(word)
                .writer(writer)
                .registerDate(LocalDateTime.now())
                .build();

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_SEARCH_WORD.getRoutingKey());
        rabbitMQPublisher.publishElasticsearch(routingKey, esSearchWord);
    }

}
