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
        		.route("person-api", r -> r.path("/person/api/**").uri("http://localhost:7075"))
        		.route("person-websocket", r -> r.path("person-api-websocket/**").uri("ws://localhost:7075"))        		
        		.route("default-route", r -> r.path("/**").uri("http://localhost:7075"))
                .build();
    }

}