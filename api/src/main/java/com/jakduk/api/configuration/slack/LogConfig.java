package com.jakduk.api.configuration.slack;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by pyohwan on 16. 11. 2.
 */

@Component
@Setter
@Getter
public class LogConfig {

    @Value("${api.slack.log.enabled}")
    private Boolean enabled;

    @Value("${api.slack.log.level}")
    private Level level;

    @Value("${api.slack.log.webhook}")
    private String webhook;

    @Value("${api.slack.log.channel}")
    private String channel;

    @Value("${api.slack.log.username}")
    private String username;
}
