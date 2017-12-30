package com.jakduk.api.restcontroller;

import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.internal.SendBulkMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/internal")
@RestController
public class InternalRestController {

    @Autowired private RabbitMQPublisher rabbitMQPublisher;

    // 단체 메일 발송
    @PostMapping("/rabbit-mq/send/bulk-mail")
    public EmptyJsonResponse sendBulkMail(@RequestBody SendBulkMailRequest request) {

        rabbitMQPublisher.sendBulk(request.getLocale(), request.getTemplateName(), request.getSubject(),
                request.getRecipientEmail(), request.getBody());

        return EmptyJsonResponse.newInstance();
    }

}
