package com.study.modoos.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("https://modoos.vercel.app");
        config.addAllowedOriginPattern("http://localhost");
        config.addAllowedHeader("*");
        config.addExposedHeader("Set-Cookie");
        config.addAllowedMethod("POST, GET, PUT, OPTIONS, DELETE, HEAD");

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
