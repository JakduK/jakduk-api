package com.jakduk.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

/**
 * Created by pyohwan on 16. 3. 30.
 */
public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        if (iLoggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
            // Send simple message in different room with custom name
            SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
            api.call(new SlackMessage("#jakduk_log", "JAKDUK", iLoggingEvent.getMessage()));
        }

    }
}
