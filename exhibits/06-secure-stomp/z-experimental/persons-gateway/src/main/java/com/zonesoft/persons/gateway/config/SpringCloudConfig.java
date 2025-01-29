package com.zonesoft.persons.gateway.config;

import org.springframework.http.HttpHeaders;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

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

@Component
@Order(0)
class LoggingFilter implements GlobalFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       
    	LOGGER.debug("Pre Filter executed");
       
        String requestPath = exchange.getRequest().getPath().toString();
        LOGGER.debug("Request path = " + requestPath);
        
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerNames = headers.keySet();
 
        headerNames.forEach((header) -> {
        	LOGGER.debug(header + " " + headers.get(header));
        });    	
    	
        return chain.filter(exchange);
    }
 
}