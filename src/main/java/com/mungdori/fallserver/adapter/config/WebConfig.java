package com.mungdori.fallserver.adapter.config;

import com.mungdori.fallserver.adapter.security.CurrentMemberArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String frontendUrl;
    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    public WebConfig(@Value("${app.frontend-url}") String frontendUrl,
                     CurrentMemberArgumentResolver currentMemberArgumentResolver) {
        this.frontendUrl = frontendUrl;
        this.currentMemberArgumentResolver = currentMemberArgumentResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<org.springframework.web.method.support.HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}

