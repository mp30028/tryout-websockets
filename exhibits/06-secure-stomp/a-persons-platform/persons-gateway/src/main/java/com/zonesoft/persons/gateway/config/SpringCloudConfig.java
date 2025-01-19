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
        		.route("persons-api", r -> r.path("/persons/api/**").uri("http://localhost:7075"))
        		.route("default-route", r -> r.path("/**").uri("http://localhost:7075"))
//        		.route("hello-world-ui", r -> r.path("/hello-world/ui/**").uri("http://localhost:8111"))
//                .route("hello-world-api-greeting", r -> r.path("/hello-world/api/greeting/**").uri("http://localhost:8112"))
//                .route("hello-world-api-calendar", r -> r.path("/hello-world/api/calendar/**").uri("http://localhost:8114"))
//                .route("hello-world-api-clock", r -> r.path("/hello-world/api/clock/**").uri("http://localhost:8113"))
//                .route("default-route", r -> r.path("/**").uri("http://localhost:8111"))
                .build();
    }

}