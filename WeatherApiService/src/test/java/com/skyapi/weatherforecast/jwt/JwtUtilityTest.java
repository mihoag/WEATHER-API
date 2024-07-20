package com.skyapi.weatherforecast.jwt;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.skyapi.weatherforecast.security.jwt.JwtUtility;
import com.skyapi.weatherforecast.security.jwt.JwtValidationException;
import com.skyapi.weatherforecast.user.role;
import com.skyapi.weatherforecast.user.user;

public class JwtUtilityTest {

	private static JwtUtility  jwtUtility;
	
	@BeforeAll
	static void setup()
	{
		jwtUtility = new JwtUtility();
		jwtUtility.setIssuerName("My Company");
		jwtUtility.setAccessTokenExpiration(2);
		jwtUtility.setSecretKey("asdfghjksaASDFGHJK123452345@#$%^&*ZXCVBNM121312312@@@#!@#k213123h12j3h");
	}
	
	@Test
	public void generateAccessTokenFail()
	{
		
		assertThrows(IllegalArgumentException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				user user = null;
				// TODO Auto-generated method stub
			   jwtUtility.generateAccessToken(user);	
			}
		});
	}
	
	@Test
	public void generateAccessTokenSuccess()
	{
		
		assertDoesNotThrow(new Executable() {
			
			@Override
			public void execute() throws Throwable {
				user user = new user();
				user.setId(1);
				user.setPassword("123456");
				user.setUsername("admin");
				
				user.addRoles(new role("READ"));
				user.addRoles(new role("DELETE"));
				
				// TODO Auto-generated method stub
		       jwtUtility.generateAccessToken(user);		
			}
		});
	}
	
	@Test
	public void validateAccessTokenFail()
	{
		assertThrows(JwtValidationException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				// TODO Auto-generated method stub
				jwtUtility.validateAccessToken("1234567sdasdad");
			}
		});
	}
	
	@Test
	public void validateAccessTokenSuccess()
	{
		user user = new user();
		user.setId(1);
		user.setPassword("123456");
		user.setUsername("admin");
		
		user.addRoles(new role("READ"));
		user.addRoles(new role("DELETE"));
		
		// TODO Auto-generated method stub
        String accessToken = jwtUtility.generateAccessToken(user);	
        
        assertDoesNotThrow(new Executable() {
			
			@Override
			public void execute() throws Throwable {
				// TODO Auto-generated method stub
				jwtUtility.validateAccessToken(accessToken);
			}
		});
	}
}
