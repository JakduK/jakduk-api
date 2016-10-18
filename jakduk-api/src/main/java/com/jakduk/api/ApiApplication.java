package com.jakduk.api;

import com.jakduk.core.CoreApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by pyohwan on 16. 10. 16.
 */

@SpringBootApplication(scanBasePackageClasses = {CoreApplication.class, ApiApplication.class})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
