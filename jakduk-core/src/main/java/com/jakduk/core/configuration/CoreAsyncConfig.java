package com.jakduk.core.configuration;

import com.jakduk.core.trigger.TokenTerminationTrigger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author pyohwan
 * 16. 4. 2 오후 11:19
 */

@Configuration
@EnableAsync
public class CoreAsyncConfig implements AsyncConfigurer {

    @Bean(initMethod = "init")
    public TokenTerminationTrigger tokenTerminationTrigger() {
        TokenTerminationTrigger tokenTerminationTrigger = new TokenTerminationTrigger();
        tokenTerminationTrigger.setSpan(5);

        return tokenTerminationTrigger;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(2);
        executor.setMaxPoolSize(5);
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Bean(name = "asyncMailExecutor")
    public Executor getAsyncMailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(3);
        executor.setThreadNamePrefix("Mail-Thread-");
        executor.initialize();

        return executor;
    }
}
