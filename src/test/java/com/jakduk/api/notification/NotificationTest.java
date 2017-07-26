package com.jakduk.api.notification;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.rabbitmq.EmailRoutingKey;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.JakdukProperties;

import com.jakduk.api.model.rabbitmq.EmailPayload;
import com.jakduk.api.service.EmailService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 9. 11.
 */
public class NotificationTest extends ApiApplicationTests {

    @Resource
    private JakdukProperties jakdukProperties;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Ignore
    @Test
    public void 메일발송() throws MessagingException {

        Locale locale = Locale.KOREAN;

        emailService.sendMailWithInline("Pyohwan", "phjang1983@daum.net", locale);
    }

    @Ignore
    @Test
    public void 가입메일() throws MessagingException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREAN)
                .type(JakdukConst.EMAIL_TYPE.WELCOME)
                .recipientEmail("phjang1983@daum.net")
                .body(
                        new HashMap<String, String>() {
                            {
                                put("username", "이은상");
                            }
                        }
                )
                .build();

        emailService.sendWelcome(emailPayload);

    }

    @Ignore
    @Test
    public void 비밀번호_갱신() throws MessagingException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREA)
                .type(JakdukConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail("phjang1983@daum.net")
                .subject("jakduk.com-" + JakdukUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
                .extra(
                        new HashMap<String, String>() {
                            {
                                put("host", "http://localhost:8080");
                            }
                        }
                )
                .body(
                        new HashMap<String, String>() {
                            {
                                put("email", "phjang1983@daum.net");
                            }
                        }
                )
                .build();

        emailService.sendResetPassword(emailPayload);

    }

    @Ignore
    @Test
    public void sendWelcomeWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREAN)
                .type(JakdukConst.EMAIL_TYPE.WELCOME)
                .recipientEmail("phjang1983@daum.net")
                .subject("K리그 작두왕에 오신것을 환영합니다.")
                .body(
                        new HashMap<String, String>() {
                            {
                                put("username", "이은상");
                            }
                        }
                )
                .build();

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_WELCOME.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

    @Ignore
    @Test
    public void sendResetPasswordWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREA)
                .type(JakdukConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail("phjang1983@daum.net")
                .subject("jakduk.com-" + JakdukUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
                .extra(
                        new HashMap<String, String>() {
                            {
                                put("host", "http://localhost:8080");
                            }
                        }
                )
                .body(
                        new HashMap<String, String>() {
                            {
                                put("email", "phjang1983@daum.net");
                            }
                        }
                )
                .build();

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

}
