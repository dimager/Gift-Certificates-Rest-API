package com.epam.ems;


import com.epam.ems.service.impl.TagServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.epam.ems")
public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);
        TagServiceImpl tagService = applicationContext.getBean(TagServiceImpl.class);
    }
}
