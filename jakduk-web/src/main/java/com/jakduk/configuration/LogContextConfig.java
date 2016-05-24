package com.jakduk.configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 4. 28.
 */

@Configuration
@Slf4j
public class LogContextConfig implements InitializingBean {

    @Resource
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Boolean enabled = environment.getProperty("slack.enabled", Boolean.class);
        String webhook = environment.getProperty("slack.log.webhook");
        String channel = environment.getProperty("slack.log.channel");
        String username = environment.getProperty("slack.log.username");
        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern("%-4relative [%thread] %-5level %class - %msg%n");
        layout.start();

        SlackAppender slackAppender = new SlackAppender(enabled, webhook, channel, username, layout);

        slackAppender.setContext(loggerContext);
        slackAppender.setName("slackAppender");
        slackAppender.start();
        loggerContext.getLogger("root").addAppender(slackAppender);

    }
}
