package com.jakduk.api;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by pyohwan on 16. 10. 16.
 */

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(ApiApplication.class, args);
    }
}
