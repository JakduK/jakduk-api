package com.jakduk.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by pyohwan on 16. 10. 19.
 */

@SpringBootApplication
@PropertySource("classpath:/application-core.properties")
public class CoreApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CoreApplication.class)
                .web(false)
                .profiles("core-local")
                .run(args);
    }
}
