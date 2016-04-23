package com.jakduk.configuration;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import ro.isdc.wro.http.WroFilter;

/**
 * Created by pyohwan on 16. 4. 2.
 */
public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));
        container.addListener(new HttpSessionEventPublisher());
        container.addListener(new SessionListener());

        registerCharcterEncodingFilter(container);
        registerSpringSecurityFilter(container);
        registerDeviceResolverRequestFilter(container);
        registerDispatcherServlet(container);
        registerWroFilter(container);

        rootContext.getEnvironment().setDefaultProfiles("local");
    }

    // UTF-8 캐릭터 인코딩 필터를 추가한다.
    public void registerCharcterEncodingFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("CHARACTER_ENCODING_FILTER", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "UTF-8");
        filter.setInitParameter("forceEncoding", "true");
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
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

    // Create the dispatcher servlet's Spring application context
    public void registerDispatcherServlet(ServletContext servletContext) {
//        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
//        appContext.setConfigLocation("classpath:/config/spring/webmvc-config.xml");

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(MvcConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/");
    }

    // Create the wro filter's Spring application context
    public void registerWroFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic wro = servletContext.addFilter("WroFilter", new WroFilter());
        wro.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/bundles/*");
    }
}

