package com.zonesoft.person.ui.configs;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.zonesoft.persons.kc.utils.KeycloakRolesConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                		.requestMatchers("/", "/index.html").hasAnyAuthority("PERSONS_PLATFORM_ADMIN", "PERSONS_PLATFORM_USER")
                		.requestMatchers("/event-viewer.html").hasAuthority("PERSONS_PLATFORM_ADMIN")
                		.anyRequest().authenticated()
                );

		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(customizer -> customizer
					.jwtAuthenticationConverter(jwtAuthenticationConverter())
				)
			);        
        return http.build();
    }
    

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRolesConverter());
        
        return jwtAuthenticationConverter;
    }    

}