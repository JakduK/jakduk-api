package com.jakduk.notification;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by pyohwan on 16. 5. 22.
 */

@Component
public class SlackService {

    @Value("${slack.enabled}")
    private boolean enabled;

    @Value("${slack.board.channel}")
    private String channel;

    @Autowired
    private SlackApi slackApi;

    public void send(String user, String message) {
        if (enabled)
            slackApi.call(new SlackMessage(channel, user, message));
    }
}
