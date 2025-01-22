package com.zonesoft.persons.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("person-api", r -> r.path("/person/api/**").uri("https://localhost:7075"))
        		.route("person-websocket", r -> r.path("person-api-websocket/**").uri("wss://localhost:7075"))
                .route("person-ui", r -> r
                        .path("/person/ui/**")
                        .filters(f->f.rewritePath("/person/ui/(?<segment>.*)","/${segment}"))
                        .uri("https://localhost:7999")
                      )                
        		.route("default-route", r -> r.path("/**").uri("https://localhost:7075"))
                .build();
    } 
}