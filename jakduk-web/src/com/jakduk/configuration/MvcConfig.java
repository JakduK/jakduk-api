package com.jakduk.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyohwan on 16. 4. 2.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.jakduk"}, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("atom", MediaType.APPLICATION_ATOM_XML);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");

        DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor = new DeviceResolverHandlerInterceptor();

        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(deviceResolverHandlerInterceptor);
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        List<ViewResolver> viewResolvers = new ArrayList<>();
        List<View> defaultViews = new ArrayList<>();

        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();

        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/views/");
        internalResourceViewResolver.setSuffix(".jsp");

        viewResolvers.add(beanNameViewResolver);
        viewResolvers.add(internalResourceViewResolver);

        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setExtractValueFromSingleKeyModel(true);

        defaultViews.add(mappingJackson2JsonView);

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(viewResolvers);
        resolver.setContentNegotiationManager(manager);
        resolver.setDefaultViews(defaultViews);

        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();

        return sessionLocaleResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/common", "classpath:messages/board", "classpath:messages/user", "classpath:messages/about",
                "classpath:messages/home", "classpath:messages/gallery", "classpath:messages/stats", "classpath:messages/search", "classpath:messages/jakdu");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(180);

        return messageSource;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(8388608);
        multipartResolver.setMaxInMemorySize(8388608);
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setUploadTempDir(new FileSystemResource("/tmp/jakduk"));

        return multipartResolver;
    }
}
