package com.jakduk.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

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

        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        converters.add(jsonConverter);

        super.configureMessageConverters(converters);
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
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREA);
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
