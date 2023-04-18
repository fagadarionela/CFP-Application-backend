package com.app.cfp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
public class CfpApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CfpApplication.class, args);
    }
}
