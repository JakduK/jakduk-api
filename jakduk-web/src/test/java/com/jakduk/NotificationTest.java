package com.jakduk;

import com.jakduk.notification.SlackService;
import com.jakduk.util.AbstractSpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by pyohwan on 16. 5. 22.
 */

public class NotificationTest extends AbstractSpringTest {

    @Value("${slack.board.webhook}")
    private String boardWebhook;

    @Value("${slack.board.channel}")
    private String boardChannel;

    @Autowired
    SlackService slackService;

    @Ignore
    @Test
    public void 슬랙알림() {
        slackService.send("test01", "hello");
    }
}
