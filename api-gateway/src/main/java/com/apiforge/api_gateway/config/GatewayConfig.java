package com.apiforge.api_gateway.config;

import com.apiforge.api_gateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:7081"))
                
                .route("content-type-service", r -> r
                        .path("/api/content-types/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:7082"))
                
                .route("content-service", r -> r
                        .path("/api/content/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:7083"))
                
                .route("media-service", r -> r
                        .path("/api/media/**")
                        // Media service has its own public endpoints logic in filter, so we still apply filter
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:7084"))
                
                .route("permission-service", r -> r
                        .path("/api/permissions/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:7085"))
                
                .build();
    }
}