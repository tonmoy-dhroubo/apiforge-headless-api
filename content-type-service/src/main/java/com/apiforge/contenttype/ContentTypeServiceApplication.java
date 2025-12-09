package com.apiforge.contenttype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.apiforge.contenttype", "com.apiforge.common"})
public class ContentTypeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentTypeServiceApplication.class, args);
    }
}
