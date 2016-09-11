package com.jakduk.core.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 3. 30.
 */

@AllArgsConstructor
@NoArgsConstructor
public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private Boolean enabled;
    private String webhook;
    private String channel;
    private String username;
    private Layout<ILoggingEvent> layout;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        if (enabled) {
            if (iLoggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
                // Send simple message in different room with custom name

                SlackApi api = new SlackApi(webhook);
                api.call(new SlackMessage(channel, username, layout.doLayout(iLoggingEvent)));
            }
        }
    }
}


