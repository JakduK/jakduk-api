package com.jakduk.configuration;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.data.rest.webmvc.RepositoryRestDispatcherServlet;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import ro.isdc.wro.http.WroContextFilter;
import ro.isdc.wro.http.WroFilter;
import ro.isdc.wro.http.WroServletContextListener;

/**
 * web.xml
 * Created by pyohwan on 16. 4. 2.
 */
public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {

        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // Manage the lifecycle of the root application context
        ctx.addListener(new ContextLoaderListener(rootContext));
        ctx.addListener(new HttpSessionEventPublisher());
        ctx.addListener(new SessionListener());
        ctx.addListener(new WroServletContextListener());
        //ctx.addListener(new TaglibServletContextListener());

        RepositoryRestDispatcherServlet exporter = new RepositoryRestDispatcherServlet();
        ServletRegistration.Dynamic reg = ctx.addServlet("rest-exporter", exporter);
        reg.setLoadOnStartup(1);
        reg.addMapping("/rest/*");

        registerCharcterEncodingFilter(ctx);
        registerSpringSecurityFilter(ctx);
        registerDeviceResolverRequestFilter(ctx);
        registerDispatcherServlet(ctx);
        //registerRestDispatcherServlet(ctx);
        registerWroFilter(ctx);
        //registerWroContextFilter(ctx);

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
    public void registerDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(MvcConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/");
    }

    // Create the dispatcher servlet's Spring application context
    public void registerRestDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(RestMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("rest", new RepositoryRestDispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/rest/*");
    }
}

