package com.jakduk.api.configuration.slack;

import ch.qos.logback.classic.LoggerContext;
import com.jakduk.api.configuration.ApiProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pyohwan on 16. 4. 28.
 */

@Configuration
public class LogContextConfig implements InitializingBean {

    @Autowired
    private ApiProperties apiProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        SlackAppender slackAppender = new SlackAppender(apiProperties.getSlackLog());
        slackAppender.setContext(loggerContext);
        slackAppender.setName("slackAppender");
        slackAppender.start();
        loggerContext.getLogger("root").addAppender(slackAppender);
    }
}
