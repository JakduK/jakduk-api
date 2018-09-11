package com.jakduk.api.common.rabbitmq;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.EsArticle;
import com.jakduk.api.model.elasticsearch.EsComment;
import com.jakduk.api.model.elasticsearch.EsGallery;
import com.jakduk.api.model.elasticsearch.EsSearchWord;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.SimpleWriter;
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
import java.util.Objects;

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
        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(locale);
        emailPayload.setType(Constants.EMAIL_TYPE.WELCOME);
        emailPayload.setTemplateName("mail/welcome");
        emailPayload.setRecipientEmail(recipientEmail);
        emailPayload.setSubject("K리그 작두왕에 오신것을 환영합니다.");
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("username", userName);
                    }
                }
        );

        String routingKey = rabbitmqProperties.getRoutingKeys().get(EmailRoutingKey.EMAIL_WELCOME.getRoutingKey());
        this.publishEmail(routingKey, emailPayload);
    }

    public void sendResetPassword(Locale locale, String recipientEmail, String host) {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(locale);
        emailPayload.setType(Constants.EMAIL_TYPE.RESET_PASSWORD);
        emailPayload.setTemplateName("mail/resetPassword");
        emailPayload.setRecipientEmail(recipientEmail);
        emailPayload.setSubject(JakdukUtils.getMessageSource("email.user.password.reset.subject"));
        emailPayload.setExtra(
                new HashMap<String, String>() {
                    {
                        put("host", host);
                    }
                }
        );
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("email", recipientEmail);
                    }
                }
        );

        String routingKey = rabbitmqProperties.getRoutingKeys().get(EmailRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());
        this.publishEmail(routingKey, emailPayload);
    }

    public void indexDocumentArticle(String id, Integer seq, String board, String category, CommonWriter writer, String subject,
                                     String content, List<String> galleryIds) {

        EsArticle esArticle = new EsArticle();
        esArticle.setId(id);
        esArticle.setSeq(seq);
        esArticle.setBoard(board);
        esArticle.setCategory(category);
        esArticle.setWriter(writer);
        esArticle.setSubject(JakdukUtils.stripHtmlTag(subject));
        esArticle.setContent(JakdukUtils.stripHtmlTag(content));
        esArticle.setGalleries(galleryIds);

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE.getRoutingKey());
        this.publishElasticsearch(routingKey, esArticle);
    }

    public void deleteDocumentArticle(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentComment(String id, ArticleItem articleItem, CommonWriter writer, String content, List<String> galleryIds) {

        EsComment esComment = new EsComment();
        esComment.setId(id);
        esComment.setArticle(articleItem);
        esComment.setContent(JakdukUtils.stripHtmlTag(content));
        esComment.setGalleries(galleryIds);

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_ARTICLE_COMMENT.getRoutingKey());
        this.publishElasticsearch(routingKey, esComment);
    }

    public void deleteDocumentComment(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_ARTICLE_COMMENT.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentGallery(String id, CommonWriter writer, String name) {
        EsGallery esGallery = new EsGallery();
        esGallery.setId(id);
        esGallery.setWriter(writer);
        esGallery.setName(name);

        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_INDEX_DOCUMENT_GALLERY.getRoutingKey());
        this.publishElasticsearch(routingKey, esGallery);
    }

    public void deleteDocumentGallery(String id) {
        String routingKey = rabbitmqProperties.getRoutingKeys().get(ElasticsearchRoutingKey.ELASTICSEARCH_DELETE_DOCUMENT_GALLERY.getRoutingKey());
        this.publishElasticsearch(routingKey, id);
    }

    public void indexDocumentSearchWord(String word, CommonWriter writer) {
        EsSearchWord esSearchWord = new EsSearchWord();
        esSearchWord.setWord(word);
        esSearchWord.setWriter(Objects.nonNull(writer) ? new SimpleWriter(writer.getUserId(), writer.getUsername()) : null);
        esSearchWord.setRegisterDate(LocalDateTime.now());

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
