package com.zonesoft.addressbook.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.zonesoft.addressbook.handlers.GreetingHandler;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GreetingRouter {

//	@Bean
//	public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
//
//		return RouterFunctions
//			.route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello);
//	}
	
    @Bean
    public RouterFunction<ServerResponse> routes(GreetingHandler greetingHandler) {
        return route(GET("/hello"), greetingHandler::hello);
    }	
}
