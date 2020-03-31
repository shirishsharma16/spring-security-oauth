package com.baeldung.newstack;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CutomAuthoritiesResourceServerLiveTest {
	
	final String resourceServerport = "8081";
	final String authServerport = "8083";
	final String redirectUrl = "http://localhost:8082/new-client/login/oauth2/code/custom";
	final String authorizeUrl = "http://localhost:" + authServerport + "/auth/realms/baeldung/protocol/openid-connect/auth?response_type=code&client_id=newClient&redirect_uri=" + redirectUrl;
	final String tokenUrl = "http://localhost:" + authServerport + "/auth/realms/baeldung/protocol/openid-connect/token";
	// final String resourceUrl = "http://localhost:" + resourceServerport + "/new-resource-server/user/info";
	final String resourceUrl = "http://localhost:" + resourceServerport + "/new-resource-server/check";

	@Test
	public void givenAccessToken_whenGetUserResourceClimPass_thenSuccess() {
		String username = "john@baeldung.com"; 
		String password = "123";

		String accessToken = obtainAccessToken(username,password);

		Response response = RestAssured.given()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.get(resourceUrl);
		assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
	}

	@Test
	public void givenAccessToken_whenGetUserResourceClaimFailsEmptyResponse_thenSuccess() {
		String username = "mike@other.com ";
		String password = "pass";

		String accessToken = obtainAccessToken(username,password);

		Response response = RestAssured.given()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.get(resourceUrl);
		assertThat(HttpStatus.FORBIDDEN.value()).isEqualTo(response.getStatusCode());
	}

	private String obtainAccessToken(String username, String password)  {

		Response response = RestAssured.given()
				.get("http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/auth?response_type=code&client_id=newClient&scope=read&state=6-gzTSRSm_Wg2XgPP9CZENipoLqbrjDGWqM3kPvFmoc%3D&redirect_uri=http://localhost:8082/new-client/login/oauth2/code/custom");
		String authSessionId = response.getCookie("AUTH_SESSION_ID");
		String kcPostAuthenticationUrl = response.asString()
				.split("action=\"")[1].split("\"")[0].replace("&amp;", "&");

		response = RestAssured.given()
				.redirects()
				.follow(false)
				.cookie("AUTH_SESSION_ID", authSessionId)
				.formParams("username", username, "password", password)
				.post(kcPostAuthenticationUrl);
		assertThat(HttpStatus.FOUND.value()).isEqualTo(response.getStatusCode());

		String location = response.getHeader(HttpHeaders.LOCATION);
		String code = location.split("code=")[1].split("&")[0];

		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("client_id", "newClient");
		params.put("redirect_uri", redirectUrl);
		params.put("client_secret", "newClientSecret");
		response = RestAssured.given()
				.formParams(params)
				.post(tokenUrl);
		return response.jsonPath()
				.getString("access_token");


	}
}

