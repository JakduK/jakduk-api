package com.jakduk.configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author pyohwan
 * 16. 4. 2 오전 12:16
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // 30 days
        httpSessionEvent.getSession().setMaxInactiveInterval(60 * 60 * 24 * 30);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
