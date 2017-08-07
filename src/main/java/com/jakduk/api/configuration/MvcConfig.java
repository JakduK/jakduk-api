package com.jakduk.api.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

/**
 * @author pyohwan
 * 16. 4. 2 오후 10:58
 */

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");

        registry.addInterceptor(localeChangeInterceptor);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    /**
     * RequestParam으로 입수되는 value에 대해서 validation체크를 할 수 있도록 해주는 Bean
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidatorFactory(localValidatorFactoryBean());

        return methodValidationPostProcessor;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // for springfox
    @Bean
    public BeanValidatorPluginsConfiguration beanValidatorPluginsConfiguration() {
        return new BeanValidatorPluginsConfiguration();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(
                "messages/common", "messages/board", "messages/user", "messages/about", "messages/home",
                "messages/gallery", "messages/search", "messages/jakdu", "messages/email", "messages/exception"
        );

        return messageSource;
    }

}
