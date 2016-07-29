package com.jakduk.configuration;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.rest.webmvc.RepositoryRestDispatcherServlet;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * web.xml
 * Created by pyohwan on 16. 4. 2.
 */
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{MvcConfig.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        // UTF-8 캐릭터 인코딩 필터를 추가한다.
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return new Filter[] {encodingFilter};
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        super.registerContextLoaderListener(servletContext);

        servletContext.addListener(new HttpSessionEventPublisher());
        servletContext.addListener(new SessionListener());
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        // 기본적으로 local로 프로파일 설정.
        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment)context.getEnvironment()).setDefaultProfiles("local");
        return context;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        registerSpringSecurityFilter(servletContext);
        registerDeviceResolverRequestFilter(servletContext);
        //registerCorsFilter(servletContext);

        //RepositoryRestDispatcherServlet exporter = new RepositoryRestDispatcherServlet();
        //ServletRegistration.Dynamic reg = ctx.addServlet("rest-exporter", exporter);
        //reg.setLoadOnStartup(1);
        //reg.addMapping("/rest/*");
    }

    // Spring Security Filter
    public void registerSpringSecurityFilter(ServletContext servletContext) {
        DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy();
        FilterRegistration.Dynamic filter = servletContext.addFilter("springSecurityFilterChain", springSecurityFilterChain);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    // DeviceResolverRequest Filter
    public void registerDeviceResolverRequestFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("deviceResolverRequestFilter", new DeviceResolverRequestFilter());
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
    }

    // DeviceResolverRequest Filter
    public void registerCorsFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("corsFilter", new CorsFilter());
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
    }

    // Create the dispatcher servlet's Spring application context
    public void registerRestDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(RestMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("rest", new RepositoryRestDispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/rest/*");
    }

}

