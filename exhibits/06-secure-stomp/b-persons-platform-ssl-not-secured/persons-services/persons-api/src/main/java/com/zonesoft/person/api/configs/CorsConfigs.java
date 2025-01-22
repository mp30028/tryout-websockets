package com.zonesoft.person.api.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfigs {
	
	
	@Value("${com.zonesoft.allowed-origins}")
	private String allowedOriginsList;

	@Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(allowedOriginsList.split(","))
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH", "HEAD")
				.allowedHeaders("Content-Type", "Origin","Accept");
			}
		};
	}
}
