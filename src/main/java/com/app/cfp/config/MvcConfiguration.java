package com.app.cfp.config;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            if (!serverPort.equals("8082")) {
                cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
            }
            context.setCookieProcessor(cookieProcessor);
        };
    }
}