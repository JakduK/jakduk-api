package com.jakduk.configuration;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;

/**
 * Created by pyohwan on 16. 4. 2.
 */

@Configuration
@ComponentScan(basePackages = {"com.jakduk"}, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@ImportResource(value = {"classpath:/config/spring/webmvc-config.xml"})
public class MvcConfig {

}
