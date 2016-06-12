package com.jakduk.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.rest.webmvc.RepositoryRestDispatcherServlet;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ro.isdc.wro.http.WroContextFilter;
import ro.isdc.wro.http.WroFilter;
import ro.isdc.wro.http.WroServletContextListener;

import javax.servlet.*;
import java.util.EnumSet;

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
        return new Class<?>[]{AppConfig.class};
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
        servletContext.addListener(new WroServletContextListener());
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
        registerWroFilter(servletContext);
        //registerWroContextFilter(ctx);

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

    // Create the wro filter's Spring application context
    public void registerWroFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic wro = servletContext.addFilter("WroFilter", new WroFilter());
        wro.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/bundles/*");
    }

    // Create the wroContext filter's Spring application context
    public void registerWroContextFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic wro = servletContext.addFilter("WroContextFilter", new WroContextFilter());
        wro.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/bundles/*");
    }

    // Create the dispatcher servlet's Spring application context
    public void registerRestDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(RestMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("rest", new RepositoryRestDispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/rest/*");
    }

}

