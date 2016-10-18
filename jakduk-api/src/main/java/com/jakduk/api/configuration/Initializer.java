package com.jakduk.api.configuration;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.rest.webmvc.RepositoryRestDispatcherServlet;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * web.xml
 * Created by pyohwan on 16. 4. 2.
 */
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 없애도 된다.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApiRootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ApiMvcConfig.class};
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
        dispatcherContext.register(ApiRestMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("rest", new RepositoryRestDispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/rest/*");
    }

}

