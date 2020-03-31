package com.baeldung.newstack;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class UsernameClaimValidator implements OAuth2TokenValidator<Jwt> {

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		OAuth2Error error = new OAuth2Error("invalid_claim");

		final String username = (String) token.getClaim("preferred_username");
		
		String[] splitUsername = username.split("@");
		if(splitUsername[1].equals("test.com")) {
			return	OAuth2TokenValidatorResult.success();
		}
		else {
			return	OAuth2TokenValidatorResult.success();
			//return OAuth2TokenValidatorResult.failure(error);
		}
	}
}
