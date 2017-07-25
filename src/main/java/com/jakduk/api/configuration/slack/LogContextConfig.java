package com.jakduk.api.configuration.slack;

import ch.qos.logback.classic.LoggerContext;
import com.jakduk.api.configuration.JakdukProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 4. 28.
 */

@Configuration
public class LogContextConfig implements InitializingBean {

    @Resource private JakdukProperties.SlackLog slackLogProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        SlackAppender slackAppender = new SlackAppender(slackLogProperties);
        slackAppender.setContext(loggerContext);
        slackAppender.setName("slackAppender");
        slackAppender.start();
        loggerContext.getLogger("root").addAppender(slackAppender);
    }
}
