package com.skyapi.weatherforecast.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationDTO;
import com.skyapi.weatherforecast.security.auth.AuthRequest;
import com.skyapi.weatherforecast.security.auth.AuthResponse;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {
	private static final String GET_ACCESS_TOKEN_ENDPOINT = "/api/oauth/token";
	private static final String LIST_LOCATIONS_ENDPOINT = "/v1/locations";
	
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	
	@Test
	public void getBaseUriShouldReturn200() throws Exception
	{
		mockMvc.perform(get("/"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testGetAccessTokenBadRequest() throws Exception
	{
		String username = "admin";
		String password = "";
		
		AuthRequest request = new AuthRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		String content = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(content))
		.andDo(print())
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetAccessTokenFail() throws Exception
	{
		String username = "admin";
		String password = "abcxyz";
		
		AuthRequest request = new AuthRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		String content = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(content))
		.andDo(print())
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetAccessTokenSuccess() throws Exception
	{
		String username = "admin";
		String password = "123456";
		
		AuthRequest request = new AuthRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		String content = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(content))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testListLocationsFail() throws Exception
	{
	     mockMvc.perform(get(LIST_LOCATIONS_ENDPOINT).header("Authorization", "Bearer somethinginvalid"))
	     .andDo(print())
	     .andExpect(status().isUnauthorized())
	     .andExpect(jsonPath("$.error[0]").isNotEmpty());
	}
	
	@Test
	public void testListLocationsSuccess() throws Exception
	{
	    AuthRequest request = new AuthRequest();
	    request.setUsername("admin");
	    request.setPassword("123456");
	    
	    String requestBody = objectMapper.writeValueAsString(request);
	    MvcResult mvcResult = mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT)
				.contentType("application/json").content(requestBody))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.access_token").isNotEmpty())
			.andExpect(jsonPath("$.refresh_token").isNotEmpty())
			.andReturn();
	    
	    String responseBody = mvcResult.getResponse().getContentAsString();
	    AuthResponse response = objectMapper.readValue(responseBody, AuthResponse.class);
	    String bearerToken = "Bearer " + response.getAccessToken();
	    mockMvc.perform(get(LIST_LOCATIONS_ENDPOINT).header("Authorization", bearerToken))
	    .andDo(print())
	    .andExpect(status().isOk());
	}
	
	@Test
	public void addLocation() throws Exception
	{
		LocationDTO locationDto = new LocationDTO();
		locationDto.setCode("NYC_USA"); 
		locationDto.setCityName("New York City");
		locationDto.setRegionName("New York");
		locationDto.setCountryCode("US");
		locationDto.setCountryName("United States of America");
		locationDto.setEnabled(true);	
		
		 AuthRequest request = new AuthRequest();
		    request.setUsername("admin");
		    request.setPassword("123456");
		    
		    String requestBody = objectMapper.writeValueAsString(request);
		    MvcResult mvcResult = mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT)
					.contentType("application/json").content(requestBody))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").isNotEmpty())
				.andExpect(jsonPath("$.refresh_token").isNotEmpty())
				.andReturn();
		    
		    String responseBody = mvcResult.getResponse().getContentAsString();
		    AuthResponse response = objectMapper.readValue(responseBody, AuthResponse.class);
		    String bearerToken = "Bearer " + response.getAccessToken();
		    
		    String content = objectMapper.writeValueAsString(locationDto);
		    
		    mockMvc.perform(post(LIST_LOCATIONS_ENDPOINT).header("Authorization", bearerToken).contentType(MediaType.APPLICATION_JSON).content(content))
		    .andDo(print())
		    .andExpect(status().isCreated());
	}

	
	@Test
	public void updateLocation() throws Exception
	{
		LocationDTO locationDto = new LocationDTO();
		locationDto.setCode("NYC_USA"); 
		locationDto.setCityName("New York City");
		locationDto.setRegionName("New York");
		locationDto.setCountryCode("US");
		locationDto.setCountryName("United States");
		locationDto.setEnabled(true);	
		
		 AuthRequest request = new AuthRequest();
		    request.setUsername("admin");
		    request.setPassword("123456");
		    
		    String requestBody = objectMapper.writeValueAsString(request);
		    MvcResult mvcResult = mockMvc.perform(post(GET_ACCESS_TOKEN_ENDPOINT)
					.contentType("application/json").content(requestBody))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").isNotEmpty())
				.andExpect(jsonPath("$.refresh_token").isNotEmpty())
				.andReturn();
		    
		    String responseBody = mvcResult.getResponse().getContentAsString();
		    AuthResponse response = objectMapper.readValue(responseBody, AuthResponse.class);
		    String bearerToken = "Bearer " + response.getAccessToken();
		    
		    String content = objectMapper.writeValueAsString(locationDto);
		    
		    mockMvc.perform(put(LIST_LOCATIONS_ENDPOINT).header("Authorization", bearerToken).contentType(MediaType.APPLICATION_JSON).content(content))
		    .andDo(print())
		    .andExpect(status().isOk());
	}
	
}
