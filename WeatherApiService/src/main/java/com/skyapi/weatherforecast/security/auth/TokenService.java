package com.skyapi.weatherforecast.security.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.skyapi.weatherforecast.security.jwt.JwtUtility;
import com.skyapi.weatherforecast.token.RefreshToken;
import com.skyapi.weatherforecast.token.RefreshTokenRepository;
import com.skyapi.weatherforecast.user.user;

@Service
public class TokenService {
	@Value("${app.security.jwt.refresh-token.expiration}")
	private int refreshTokenExpiration;
	
	@Autowired RefreshTokenRepository refreshTokenRepo;
	
	@Autowired JwtUtility jwtUtil;
	
	@Autowired PasswordEncoder passwordEncoder;
	
	public AuthResponse generateTokens(user user) {
		String accessToken = jwtUtil.generateAccessToken(user);
		
		AuthResponse response = new AuthResponse();
		response.setAccessToken(accessToken);
		
		String randomUUID = UUID.randomUUID().toString();
		
		response.setRefreshToken(randomUUID);
		
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setU(user);
		refreshToken.setToken(passwordEncoder.encode(randomUUID));
		
		long refreshTokenExpirationInMillis = System.currentTimeMillis() + refreshTokenExpiration * 60000;
		refreshToken.setExpiryTime(new Date(refreshTokenExpirationInMillis));
		
		refreshTokenRepo.save(refreshToken);
		
		return response;
	}
	
	public AuthResponse refreshTokens(RequestTokenRequest request) throws RefreshTokenNotFoundException, RefreshTokenExpireException
	{
		AuthResponse response = new AuthResponse();
		
		String username = request.getUsername();
		String rawRefreshToken = request.getRefreshToken();
		
	   RefreshToken refreshTokenFound = null;
	   List<RefreshToken> refreshTokens = refreshTokenRepo.findByUsername(username);
	   for(RefreshToken refreshToken : refreshTokens)
	   {
		   if(passwordEncoder.matches(rawRefreshToken, refreshToken.getToken()))
		   {
			  refreshTokenFound = refreshToken;
		   }
	   }
	   if(refreshTokenFound == null)
	   {
		   throw new RefreshTokenNotFoundException();
	   }
		
	   Date currentTime = new Date();
	   if(refreshTokenFound.getExpiryTime().before(currentTime))
	   {
		   throw new RefreshTokenExpireException();
	   }
	   
	   response = generateTokens(refreshTokenFound.getU());
	   return response;
	}
}
