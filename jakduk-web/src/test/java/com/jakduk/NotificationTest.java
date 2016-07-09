package com.jakduk;

import com.google.api.services.gmail.model.Message;
import com.jakduk.notification.SlackService;
import com.jakduk.util.AbstractSpringTest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * Created by pyohwan on 16. 5. 22.
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

    @Ignore
    @Test
    public void 슬랙알림() {
        slackService.send("test01", "hello");
    }

    @Test
    public void 메일발송() throws IOException {

        Configuration cfg = freeMarkerConfigurer.getConfiguration();


        Template template = cfg.getTemplate("hello.ftl");

        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, null));
        } catch (TemplateException e) {
            e.printStackTrace();
        }




        System.out.println("content=" + content);

    }
}
