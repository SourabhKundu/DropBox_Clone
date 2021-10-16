package com.dropbox.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dropbox"})
public class DropBoxMainApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DropBoxMainApplication.class, args);
    }

}
