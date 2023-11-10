package com.iwa.recrutements.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // or specific endpoints
                        //.allowedOrigins("http://yourfrontend.com", "http://localhost:3000") // origins you want to allow
                        .allowedOriginPatterns("*") // origins you want to allow
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // HTTP methods to allow
                        .allowedHeaders("*") // headers you want to allow
                        .allowCredentials(true)
                        //.allowCredentials(false)
                        .maxAge(3600); // how long, in seconds, the response from a pre-flight request can be cached by clients
            }
        };
    }
}
