package com.jakduk.api;

import com.jakduk.core.CoreApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by pyohwan on 16. 10. 16.
 */

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .parent(CoreApplication.class)
                .child(ApiApplication.class)
                .run(args);
    }
}
