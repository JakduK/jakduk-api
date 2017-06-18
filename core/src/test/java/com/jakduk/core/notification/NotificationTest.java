package com.jakduk.core.notification;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.core.common.rabbitmq.RabbitMQRoutingKey;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.common.util.SlackUtils;
import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.model.rabbitmq.EmailPayload;
import com.jakduk.core.service.CommonEmailService;
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
public class NotificationTest extends CoreApplicationTests {

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private SlackUtils slackUtils;

    @Autowired
    private CommonEmailService commonEmailService;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Ignore
    @Test
    public void 슬랙알림() {
        slackUtils.send("test01", "hello");
    }

    @Ignore
    @Test
    public void 메일발송() throws MessagingException {

        Locale locale = Locale.KOREAN;

        commonEmailService.sendMailWithInline("Pyohwan", "phjang1983@daum.net", locale);
    }

    @Ignore
    @Test
    public void 가입메일() throws MessagingException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREAN)
                .type(CoreConst.EMAIL_TYPE.WELCOME)
                .recipientEmail("phjang1983@daum.net")
                .body(
                        new HashMap<String, String>() {
                            {
                                put("username", "이은상");
                            }
                        }
                )
                .build();

        commonEmailService.sendWelcome(emailPayload);

    }

    @Ignore
    @Test
    public void 비밀번호_갱신() throws MessagingException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREA)
                .type(CoreConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail("phjang1983@daum.net")
                .subject("jakduk.com-" + CoreUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
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

        commonEmailService.sendResetPassword(emailPayload);

    }

    @Ignore
    @Test
    public void sendWelcomeWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREAN)
                .type(CoreConst.EMAIL_TYPE.WELCOME)
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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(RabbitMQRoutingKey.EMAIL_WELCOME.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

    @Ignore
    @Test
    public void sendResetPasswordWithRabbitMQ() throws InterruptedException {

        EmailPayload emailPayload = EmailPayload.builder()
                .locale(Locale.KOREA)
                .type(CoreConst.EMAIL_TYPE.RESET_PASSWORD)
                .recipientEmail("phjang1983@daum.net")
                .subject("jakduk.com-" + CoreUtils.getResourceBundleMessage("messages.user", "user.password.reset.instructions"))
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

        String routingKey = coreProperties.getRabbitmq().getRoutingKeys().get(RabbitMQRoutingKey.EMAIL_RESET_PASSWORD.getRoutingKey());

        rabbitMQPublisher.publishEmail(routingKey, emailPayload);

        Thread.sleep(1000 * 5);
    }

}
