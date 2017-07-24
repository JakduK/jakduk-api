package com.jakduk.api.common.util;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pyohwan
 * 16. 5. 22 오후 11:05
 */

@Component
public class SlackUtils {

    @Value("${core.slack.board.enabled}")
    private boolean enabled;

    @Value("${core.slack.board.channel}")
    private String channel;

    @Autowired
    private SlackApi slackApi;

    public void send(String user, String message) {
        if (enabled)
            slackApi.call(new SlackMessage(channel, user, message));
    }

    public void sendPost(String author, String title, String description, String link) {
        if (!enabled) {
            return;
        }

        SlackAttachment attachment = new SlackAttachment();
        attachment
          .setAuthorName(author)
          .setTitle(title)
          .setTitleLink(link)
          .setPretext(description)
          .setFallback(title);

        List<SlackAttachment> attachmentList = new ArrayList<>();
        attachmentList.add(attachment);

        SlackMessage slackMessage = new SlackMessage(channel, null, "");
        slackMessage.setAttachments(attachmentList);
        slackApi.call(slackMessage);
    }
}
