package com.skyapi.weatherforecast.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skyapi.weatherforecast.security.auth.AuthRequest;
import com.skyapi.weatherforecast.security.auth.AuthResponse;
import com.skyapi.weatherforecast.security.auth.RefreshTokenExpireException;
import com.skyapi.weatherforecast.security.auth.RefreshTokenNotFoundException;
import com.skyapi.weatherforecast.security.auth.RequestTokenRequest;
import com.skyapi.weatherforecast.security.auth.TokenService;
import com.skyapi.weatherforecast.user.UserRepository;
import com.skyapi.weatherforecast.user.user;

@SpringBootTest
public class TokenServiceTest {
    @Autowired private TokenService service;
    @Autowired private UserRepository userRepo;
    
    @Test
    public void getRefreshTokenFail()
    {
    	String username = "invalidUsername";
    	String refreshToken = "invalidRefreshToken";
    	
    	RequestTokenRequest request = new RequestTokenRequest(username, refreshToken);
    	assertThrows(RefreshTokenNotFoundException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				// TODO Auto-generated method stub
			    service.refreshTokens(request);
			}
		});
    }
    
    @Test
    public void getRefreshTokenSuccess() throws RefreshTokenNotFoundException, RefreshTokenExpireException
    {
    	String username = "admin";
    	user u = userRepo.getUserByUsername(username);
 	    
 	    AuthResponse response = service.generateTokens(u);
 	    String refreshToken  = response.getRefreshToken();
 	    
 	   RequestTokenRequest request = new RequestTokenRequest(username, refreshToken);
 	   AuthResponse responseRs = service.refreshTokens(request);
 	   assertThat(responseRs).isNotNull();
 	   assertThat(responseRs.getRefreshToken()).isNotNull();
 	   assertThat(responseRs.getAccessToken()).isNotNull();
    }
}
