package com.baeldung.newstack.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.baeldung.newstack.CustomAuthorityBuilder;
import com.baeldung.newstack.UsernameClaimValidator;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeRequests()
              .antMatchers(HttpMethod.GET, "/user/info", "/api/projects/**")
                .hasAuthority("SCOPE_read")
              .antMatchers(HttpMethod.POST, "/api/projects")
                .hasAuthority("SCOPE_write")
               .antMatchers("/check")
                .hasAuthority("SCOPE_SUPERUSER")
              .anyRequest()
                .authenticated()
            .and()
              .oauth2ResourceServer()
                .jwt();
    }//@formatter:on
    
    
    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
             //   JwtDecoders.fromOidcIssuerLocation("http://localhost:8083/auth/realms/baeldung");
        		JwtDecoders.fromOidcIssuerLocation(issuerUri);

       // OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer("http://localhost:8083/auth/realms/baeldung");
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, getCustomJwtClaimVerifier());

        jwtDecoder.setJwtValidator(withAudience);
        jwtDecoder.setClaimSetConverter(new CustomAuthorityBuilder());
        return jwtDecoder;
    }
   
    @Bean
    public OAuth2TokenValidator<Jwt> getCustomJwtClaimVerifier() {
        return new UsernameClaimValidator();
    }
    

}