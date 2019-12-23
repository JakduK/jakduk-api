package com.jakduk.api.notification;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.EmailRoutingKey;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.mail.EmailService;
import com.jakduk.api.model.rabbitmq.EmailPayload;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 9. 11.
 */
public class NotificationTest extends ApiApplicationTests {

    @Resource private JakdukProperties jakdukProperties;

    @Autowired private EmailService emailService;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;

    @Disabled
    @Test
    public void 메일발송() throws MessagingException {

        Locale locale = Locale.KOREAN;

        emailService.sendMailWithInline("Pyohwan", "phjang1983@daum.net", locale);
    }

    @Disabled
    @Test
    public void 가입메일() throws MessagingException {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(Locale.KOREAN);
        emailPayload.setType(Constants.EMAIL_TYPE.WELCOME);
        emailPayload.setRecipientEmail("phjang1983@daum.net");
        emailPayload.setSubject("가입 메일 연습");
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("username", "이은상");
                    }
                }
        );

        emailService.sendBulk(emailPayload);
    }

    @Disabled
    @Test
    public void 비밀번호_갱신() throws MessagingException {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(Locale.KOREA);
        emailPayload.setType(Constants.EMAIL_TYPE.RESET_PASSWORD);
        emailPayload.setRecipientEmail("phjang1983@daum.net");
        emailPayload.setSubject("jakduk.com-" + JakdukUtils.getMessageSource("email.user.password.reset.about"));
        emailPayload.setExtra(
                new HashMap<String, String>() {
                    {
                        put("host", "http://localhost:8080");
                    }
                }
        );
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("email", "phjang1983@daum.net");
                    }
                }
        );

        emailService.sendResetPassword(emailPayload);
    }

    @Disabled
    @Test
    public void testSendBulk() throws MessagingException {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(Locale.KOREAN);
        emailPayload.setType(Constants.EMAIL_TYPE.BULK);
        emailPayload.setTemplateName("mail/bulk01");
        emailPayload.setRecipientEmail("phjang1983@daum.net");
        emailPayload.setSubject("단체 메일 연습");
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("username", "이은상");
                    }
                }
        );

        emailService.sendBulk(emailPayload);
    }

    @Disabled
    @Test
    public void sendWelcomeWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(Locale.KOREAN);
        emailPayload.setType(Constants.EMAIL_TYPE.WELCOME);
        emailPayload.setRecipientEmail("phjang1983@daum.net");
        emailPayload.setSubject("K리그 작두왕에 오신것을 환영합니다.");
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("username", "이은상");
                    }
                }
        );

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_WELCOME.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

    @Disabled
    @Test
    public void sendResetPasswordWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setLocale(Locale.KOREA);
        emailPayload.setType(Constants.EMAIL_TYPE.RESET_PASSWORD);
        emailPayload.setRecipientEmail("phjang1983@daum.net");
        emailPayload.setSubject("jakduk.com-" + JakdukUtils.getMessageSource("email.user.password.reset.about"));
        emailPayload.setExtra(
                new HashMap<String, String>() {
                    {
                        put("host", "http://localhost:8080");
                    }
                }
        );
        emailPayload.setBody(
                new HashMap<String, Object>() {
                    {
                        put("email", "phjang1983@daum.net");
                    }
                }
        );

        String routingKey = jakdukProperties.getRabbitmq().getRoutingKeys().get(EmailRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

}
