package com.jakduk.api.configuration.slack;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.jakduk.api.configuration.JakdukProperties;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pyohwan on 16. 3. 30.
 */

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private JakdukProperties.SlackLog apiSlackLogProperties;

    public SlackAppender(JakdukProperties.SlackLog apiSlackLogProperties) {
        this.apiSlackLogProperties = apiSlackLogProperties;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        if (apiSlackLogProperties.getEnabled()) {
            if (iLoggingEvent.getLevel().isGreaterOrEqual(apiSlackLogProperties.getLevel())) {

                List<SlackField> fields = new ArrayList<>();

                SlackField date = new SlackField();
                date.setTitle("발생시간");
                date.setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                date.setShorten(true);
                fields.add(date);

                SlackAttachment slackAttachment = new SlackAttachment();
                slackAttachment.setFallback("옐로우 카드!!");
                slackAttachment.setColor("danger");
                slackAttachment.setFields(fields);
                slackAttachment.setTitle(iLoggingEvent.getFormattedMessage());

                if (! ObjectUtils.isEmpty(iLoggingEvent.getThrowableProxy()))
                    slackAttachment.setText(getStackTrace(iLoggingEvent.getThrowableProxy().getStackTraceElementProxyArray()));

                SlackMessage slackMessage = new SlackMessage("");
                slackMessage.setChannel("#" + apiSlackLogProperties.getChannel());
                slackMessage.setUsername(apiSlackLogProperties.getUsername());
                slackMessage.setIcon(":exclamation:");
                slackMessage.setAttachments(Collections.singletonList(slackAttachment));

                SlackApi api = new SlackApi(apiSlackLogProperties.getWebhook());
                api.call(slackMessage);
            }
        }
    }

    private String getStackTrace(StackTraceElementProxy[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (StackTraceElementProxy element : stackTraceElements) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}


