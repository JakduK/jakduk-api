package com.jakduk.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.accept.ContentNegotiationManager;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author pyohwan
 * 16. 4. 2 오후 10:58
 */

@EnableWebMvc
@Configuration
public class ApiMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        // swagger
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
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

/*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "https://staging.jakduk.com", "https://jakduk.com")
                .allowedMethods(
                        HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", tokenHeader)
                .allowCredentials(CrossOrigin.DEFAULT_ALLOW_CREDENTIALS);
    }
*/

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
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

    /**
     * RequestParam으로 입수되는 value에 대해서 validation체크를 할 수 있도록 해주는 Bean
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
