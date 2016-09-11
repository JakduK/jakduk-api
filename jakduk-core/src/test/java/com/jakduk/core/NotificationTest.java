package com.jakduk.core;

import com.jakduk.core.notification.EmailService;
import com.jakduk.core.notification.SlackService;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 9. 11.
 */
public class NotificationTest extends AbstractSpringTest {

    @Autowired
    private SlackService slackService;

    @Autowired
    private EmailService emailService;

    @Ignore
    @Test
    public void 슬랙알림() {
        slackService.send("test01", "hello");
    }

    @Ignore
    @Test
    public void 메일발송() throws MessagingException {

        Locale locale = Locale.KOREAN;

        emailService.sendMailWithInline("Pyohwan", "phjang1983@daum.net", locale);

    }
}
