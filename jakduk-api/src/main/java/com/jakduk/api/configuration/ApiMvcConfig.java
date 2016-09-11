package com.jakduk.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
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
 * @author pyohwan
 * 16. 4. 2 오후 10:58
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.jakduk.api"}, useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@Import(SwaggerConfig.class)
@PropertySource("classpath:/properties/api-${spring.profiles.active}.properties")
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
