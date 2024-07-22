package com.skyapi.weatherforecast.security.auth;


public class RequestTokenRequest {

	private String username;
	private String refreshToken;
	
	public RequestTokenRequest() {
	}

	public RequestTokenRequest(String username, String refreshToken) {
		super();
		this.username = username;
		this.refreshToken = refreshToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
