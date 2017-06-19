package com.jakduk.core.listener;

import com.jakduk.core.model.rabbitmq.ElasticsearchPayload;
import com.jakduk.core.service.CommonSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created by pyohwanjang on 2017. 6. 20..
 */

@Slf4j
@Component
public class ElasticsearchListener {

    @Autowired
    private CommonSearchService commonSearchService;

    @RabbitListener(queues = "${core.rabbitmq.queues.elasticsearch.queue-name}")
    public void receive(ElasticsearchPayload payload) throws MessagingException {

        switch (payload.getType()) {
            case INDEX_DOCUMENT_BOARD:
                commonSearchService.indexDocumentBoard(payload);
                break;
            case DELETE_DOCUMENT_BOARD:
                break;
        }
    }
}
