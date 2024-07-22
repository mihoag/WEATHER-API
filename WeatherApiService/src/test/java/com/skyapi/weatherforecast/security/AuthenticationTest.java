package com.skyapi.weatherforecast.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.skyapi.weatherforecast.security.auth.RequestTokenRequest;

@SpringBootTest
public class AuthenticationTest {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Test
	public void testAuthenticatonFail()
	{
		assertThrows(BadCredentialsException.class, () ->
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin", "xxx"));
		});
	}
	
	@Test
	public void testAuthenticationSuccess()
	{
		String username = "admin";
		String password = "123456";
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		assertThat(authentication.isAuthenticated()).isTrue();
		
		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		
		assertThat(userDetail.getUsername()).isEqualTo(username);
	}
	

	
}
