package com.jakduk.api.configuration;

import com.jakduk.core.configuration.CoreRootConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

/**
 * applicationContext.xml
 * Created by pyohwan on 16. 4. 2.
 */

@ComponentScan(basePackages = {"com.jakduk.api"},
        excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@Import({CoreRootConfig.class})
@Configuration
public class ApiRootConfig {

}
