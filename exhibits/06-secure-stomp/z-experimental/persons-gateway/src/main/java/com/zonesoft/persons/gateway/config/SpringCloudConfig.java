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
        		.route("person-api", 
        				r -> r.path("/person/api/**")
        				.filters(f->f.tokenRelay())
        				.uri("https://localhost:7075")
        				 
        		)
        		.route("person-websocket", 
        				r -> r.path("person-api-websocket/**")
        				.filters(f->f.tokenRelay())
        				.uri("wss://localhost:7075")
        		)
//        		.route("hello-world-ui", r -> r.path("/hello-world/ui/**").filters(f -> f.tokenRelay()).uri("https://localhost:8311"))
                .route("person-ui", r -> r
                        .path("/person/ui/**")
                        .filters(f->f.tokenRelay().rewritePath("/person/ui/(?<segment>.*)","/${segment}"))
                        .uri("https://localhost:7999")
                      )                
        		.route("default-route", r -> r.path("/**").filters(f->f.tokenRelay()).uri("https://localhost:7075"))
                .build();
    } 
}