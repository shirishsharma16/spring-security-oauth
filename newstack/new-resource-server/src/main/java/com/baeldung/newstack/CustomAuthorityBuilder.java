package com.baeldung.newstack;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.Map;
public class CustomAuthorityBuilder	implements Converter<Map<String, Object>, Map<String, Object>> {

	private final MappedJwtClaimSetConverter mappedJwtClaimSetConverter = 
			MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

	public Map<String, Object> convert(Map<String, Object> claims) {
		Map<String, Object> localClaims = this.mappedJwtClaimSetConverter.convert(claims);

		final String username = (String) localClaims.get("preferred_username");
		String[] splitUsername = username.split("@");
		if(splitUsername[1].equals("baeldung.com")) {
			String scopes = (String) localClaims.get("scope");
			scopes = scopes + " SUPERUSER";
			localClaims.put("scope", scopes);
		}
		return localClaims;
	}

}
