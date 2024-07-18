package com.skyapi.weatherforecast.security.jwt;

import java.io.IOException;
import java.util.Set;

import javax.management.relation.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.skyapi.weatherforecast.security.CustomUserDetails;
import com.skyapi.weatherforecast.user.role;
import com.skyapi.weatherforecast.user.user;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter{

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	@Autowired JwtUtility jwtUtil;
	
	@Autowired @Qualifier("handlerExceptionResolver")	
	HandlerExceptionResolver exceptionResolver;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// this line is for debug purpose, should be removed later
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				
				if (!hasAuthorizationBearer(request)) {
					filterChain.doFilter(request, response);
					return;
				}
				
				String token = getBearerToken(request);
				
				LOGGER.info("Token: " + token);
				
				try {
					Claims claims = jwtUtil.validateAccessToken(token);
					UserDetails userDetails = getUserDetails(claims);
					
					setAuthenticationContext(userDetails, request);
					
					filterChain.doFilter(request, response);
					
					clearAuthenticationContext();
					
				} catch (JwtValidationException e) {
					LOGGER.error(e.getMessage(), e);
					
					exceptionResolver.resolveException(request, response, null, e);
				}
		
	}

	private void clearAuthenticationContext() {
		SecurityContextHolder.clearContext();
	}

	private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
		var authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private UserDetails getUserDetails(Claims claims) {
		String subject = (String) claims.get(Claims.SUBJECT);
		String[] array = subject.split(",");
		
		Integer userId = Integer.valueOf(array[0]);
		String username = array[1];
		
		user user = new user();
		user.setId(userId);
		user.setUsername(username);
		
		Set<role> roles =  (Set<role>) claims.get("roles");
		user.setRoles(roles);
		

		return new CustomUserDetails(user);
	}

	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		LOGGER.info("Authorization Header: " + header);
		
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		return true;
	}
	
	private String getBearerToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		String[] array = header.split(" ");
		if (array.length == 2) return array[1];
		
		return null;
	}
	
	

}
