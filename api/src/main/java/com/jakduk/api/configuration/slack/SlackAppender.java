package com.jakduk.api.configuration.slack;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.util.ContextUtil;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.util.ObjectUtils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pyohwan on 16. 3. 30.
 */

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private LogConfig logConfig;

    public SlackAppender(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        if (logConfig.getEnabled()) {
            if (iLoggingEvent.getLevel().isGreaterOrEqual(logConfig.getLevel())) {

                List<SlackField> fields = new ArrayList<>();

                SlackField hostName = new SlackField();
                hostName.setTitle("호스트명");
                hostName.setValue(getHostName());
                hostName.setShorten(false);
                fields.add(hostName);

                SlackAttachment slackAttachment = new SlackAttachment();
                slackAttachment.setFallback("옐로우 카드!!");
                slackAttachment.setColor("danger");
                slackAttachment.setFields(fields);
                slackAttachment.setTitle(iLoggingEvent.getFormattedMessage());

                SlackMessage slackMessage = new SlackMessage("");
                slackMessage.setChannel("#" + logConfig.getChannel());
                slackMessage.setUsername(logConfig.getUsername());
                slackMessage.setIcon(":exclamation:");
                slackMessage.setAttachments(Collections.singletonList(slackAttachment));

                if (! ObjectUtils.isEmpty(iLoggingEvent.getThrowableProxy()))
                    slackMessage.setText(getStackTrace(iLoggingEvent.getThrowableProxy().getStackTraceElementProxyArray()));

                SlackApi api = new SlackApi(logConfig.getWebhook());
                api.call(slackMessage);
            }
        }
    }

    private String getHostName() {
        try {
            return ContextUtil.getLocalHostName();
        } catch (UnknownHostException | SocketException e) {
            return "UNKNOWN";
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


