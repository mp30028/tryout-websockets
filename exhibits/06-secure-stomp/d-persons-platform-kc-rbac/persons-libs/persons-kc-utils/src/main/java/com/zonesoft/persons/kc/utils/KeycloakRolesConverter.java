package com.zonesoft.persons.kc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakRolesConverter.class);

	public static final String ROLE_PREFIX = "ROLE_";
	private static final String RESOURCE_ACCESS = "resource_access";
	private static final String REALM_ACCESS = "realm_access";
	private static final String ROLES = "roles";
	private static final String OAUTH2_CLIENT_ID = "azp";
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<GrantedAuthority> convert(final Jwt jwt) {
		return (Collection<GrantedAuthority>) extractRoles(jwt);
	}
	
	private Collection<? extends GrantedAuthority> extractRoles(Jwt idToken) {
		LOGGER.debug("extractRoles from  idToken invoked. idToke={}", idToken);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		Map<String, Object> claims = idToken.getClaims();
		debugLogClaims("extractRoles", claims);
		extractKeycloakClientRoles(claims, grantedAuthorities);
		extractKeycloakRealmRoles(claims, grantedAuthorities);
		return grantedAuthorities;		
	}
	
	@SuppressWarnings("unchecked")
	private void extractKeycloakClientRoles(Map<String, Object> claims, Collection<GrantedAuthority> grantedAuthorities) {
        Map<String, Object> clientRoleClaims = (Map<String, Object>) claims.get(RESOURCE_ACCESS);
        String oauth2ClientId = (String) claims.get(OAUTH2_CLIENT_ID);
        LOGGER.debug("FROM extractKeycloakClientRoles: function invoked with oauth2ClientId={}",oauth2ClientId);
        debugLogClaims("extractKeycloakClientRoles", claims);
        Collection<String> clientRoles = null;
        if (Objects.nonNull(oauth2ClientId) && Objects.nonNull(clientRoleClaims)) {
        	LOGGER.debug("FROM extractKeycloakClientRoles: will attempt extracting client roles");
        	clientRoles = (Collection<String>) ((Map<String, Object>) clientRoleClaims.get(oauth2ClientId)).get(ROLES);
        	if(Objects.nonNull(clientRoles)) {
        		LOGGER.debug("FROM extractKeycloakClientRoles: client roles found in claims, starting extraction");
        		Set<SimpleGrantedAuthority> authorities = clientRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
        		grantedAuthorities.addAll(authorities);
        	}else{
        		LOGGER.debug("FROM extractKeycloakClientRoles: no client roles were found in the token");
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	private void extractKeycloakRealmRoles(Map<String, Object> claims, Collection<GrantedAuthority> grantedAuthorities) {
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS);
        Collection<String> realmRoles;
        if (Objects.nonNull(realmAccess)) {
        	realmRoles = (Collection<String>) ((Map<String, Object>) realmAccess).get(ROLES);
        	if(Objects.nonNull(realmRoles)) {        	
        		Set<SimpleGrantedAuthority> authorities =realmRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
        		grantedAuthorities.addAll(authorities);
        	}
        }
	}	
		
	private void debugLogClaims(String label, Map<String, Object> claims) {
		if (LOGGER.isDebugEnabled()) {
			for(String claim : claims.keySet()) {
				LOGGER.debug("FROM: {}: {} = {}",label, claim, claims.get(claim));
			}
		}
	}  	
}
