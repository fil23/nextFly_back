package com.nextfly.demo.configuration.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nextfly.demo.filters.PublicFilter;

@Configuration
public class PublicFilterConfig {
    @Bean
    public FilterRegistrationBean<PublicFilter> publicAreaFilter() {
        FilterRegistrationBean<PublicFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PublicFilter());
        registrationBean.addUrlPatterns("/api/auth/*"); // Filtra le richieste verso /public/*
        return registrationBean;
    }
}
