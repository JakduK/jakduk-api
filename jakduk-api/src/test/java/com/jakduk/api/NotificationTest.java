package com.jakduk.api;

import com.jakduk.api.notification.EmailService;
import com.jakduk.api.notification.SlackService;
import com.jakduk.api.util.AbstractSpringTest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * @author pyohwan
 * 16. 5. 22 오후 10:50
 */

public class NotificationTest extends AbstractSpringTest {

    @Value("${slack.board.webhook}")
    private String boardWebhook;

    @Value("${slack.board.channel}")
    private String boardChannel;

    @Autowired
    private SlackService slackService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Ignore
    @Test
    public void 슬랙알림() {
        slackService.send("test01", "hello");
    }

    @Test
    public void 메일발송() throws IOException, MessagingException {

        Configuration cfg = freeMarkerConfigurer.getConfiguration();

        Template template = cfg.getTemplate("hello.ftl");

        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, null));
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("phjang1983@daum.net");
        helper.setText(content.toString(), true);

        //javaMailSender.send(message);

        emailService.sendResetPassword("jakduk.com", "phjang1983@daum.net");




        System.out.println("content=" + content);

    }
}
