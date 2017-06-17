package com.jakduk.api.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.core.common.rabbitmq.RabbitMQRoutingKey;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.model.rabbitmq.EmailPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Service
public class EmailService {

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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(RabbitMQRoutingKey.EMAIL_WELCOME.getRoutingKey());
        rabbitMQPublisher.emailPublish(routingKey, emailPayload);
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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(RabbitMQRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());
        rabbitMQPublisher.emailPublish(routingKey, emailPayload);
    }

}
