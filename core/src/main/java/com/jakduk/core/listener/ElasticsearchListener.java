package com.jakduk.core.listener;

import com.jakduk.core.common.rabbitmq.ElasticsearchRoutingKey;
import com.jakduk.core.common.util.ObjectMapperUtils;
import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.elasticsearch.EsBoard;
import com.jakduk.core.service.CommonSearchService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class ElasticsearchListener {

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private CommonSearchService commonSearchService;

    @RabbitListener(queues = "${core.rabbitmq.queues.elasticsearch.binding-queue-name}")
    public void receive(Message message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws IOException {

        String findKey = coreProperties.getRabbitmq().getRoutingKeys().entrySet().stream()
                .filter(entity -> entity.getValue().equals(routingKey))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new ServiceException(ServiceError.ILLEGAL_ARGUMENT));

        ElasticsearchRoutingKey elasticsearchRoutingKey = ElasticsearchRoutingKey.find(findKey);

        switch (elasticsearchRoutingKey) {
            case ELASTICSEARCH_INDEX_DOCUMENT_BOARD:
                EsBoard esBoard = ObjectMapperUtils.readValue(message.getBody(), EsBoard.class);
                commonSearchService.indexDocumentBoard(esBoard);
                break;
            case ELASTICSEARCH_DELETE_DOCUMENT_BOARD:
                String id = ObjectMapperUtils.readValue(message.getBody(), String.class);
                commonSearchService.deleteDocumentBoard(id);
                break;

        }
    }

}
