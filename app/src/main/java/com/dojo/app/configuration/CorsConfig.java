package com.dojo.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders(
//                                "Content-Type",
//                                "Authorization",
//                                "traceparent",      // ← crítico para OTEL
//                                "tracestate",       // ← crítico para OTEL
//                                "baggage"           // ← usado por algunos instrumentadores
//                        )
//                        .exposedHeaders(
//                                "traceparent",      // ← para que el frontend pueda leerlo
//                                "tracestate"
//                        )
//                        .allowCredentials(false)
//                        .maxAge(7200);
//            }
//        };
//    }
//}


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    public CorsConfig() {
        System.out.println(">>> CorsConfig cargado <<<");
    }

    @Bean
    public CorsFilter corsFilter() {
        System.out.println(">>> CorsFilter bean creado <<<");
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Content-Type",
                "Authorization",
                "traceparent",   // ← OTEL
                "tracestate",    // ← OTEL
                "baggage"        // ← OTEL
        ));
        config.setExposedHeaders(List.of(
                "traceparent",
                "tracestate"
        ));
        config.setAllowCredentials(false);
        config.setMaxAge(7200L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}