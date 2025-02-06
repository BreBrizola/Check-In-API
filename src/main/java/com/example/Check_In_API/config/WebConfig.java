package com.example.Check_In_API.config;

import com.example.Check_In_API.dtos.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @SessionScope
    public Session getSession(){
        return new Session();
    }
}