package com.zonesoft.persons.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http, ServerLogoutSuccessHandler handler) throws Exception {
      http.csrf(ServerHttpSecurity.CsrfSpec::disable);		
		http.authorizeExchange(authz -> authz.anyExchange().authenticated());
		http.oauth2Client(Customizer.withDefaults());
		http.oauth2Login(Customizer.withDefaults());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.logout(customiser -> customiser.logoutSuccessHandler(handler));
		return http.build();
	}


	@Bean
	public ServerLogoutSuccessHandler keycloakLogoutSuccessHandler(ReactiveClientRegistrationRepository repository) {
		OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(repository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/person/ui/");
		return oidcLogoutSuccessHandler;
	}
	
}
