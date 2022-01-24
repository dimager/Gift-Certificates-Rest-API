package com.epam.ems;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

//public class Main implements WebApplicationInitializer {
public class Main extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

//    @Override
//    public void onStartup(ServletContext container) {
//        AnnotationConfigWebApplicationContext appContext =
//                new AnnotationConfigWebApplicationContext();
//        appContext.register(AppConfig.class);
//        ServletRegistration.Dynamic servletRegistration = container.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(appContext));
//        servletRegistration.setLoadOnStartup(1);
//        servletRegistration.addMapping("/");
//
//    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {AppConfig.class};
    }
}


