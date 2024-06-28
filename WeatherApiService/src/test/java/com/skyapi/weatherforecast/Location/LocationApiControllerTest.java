package com.skyapi.weatherforecast.Location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationApiController;
import com.skyapi.weatherforecast.location.LocationDTO;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTest {
     private static final String END_POINT_PATH = "/v1/locations";
     private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
 	 private static final String REQUEST_CONTENT_TYPE = "application/json";	
 	 
     @Autowired
     MockMvc mockMvc;
     
     @Autowired
     ObjectMapper mapper;
     
     @MockBean
     ModelMapper modelMappper;
     
     @MockBean
     LocationService service;
     
     @Test
     public void testAddShouldReturn400BadRequest() throws Exception
     {
    	 Location dto = new Location();
    	 
    	 String content = mapper.writeValueAsString(dto);
    	 
    	 System.out.println(content);
    	 
    	 mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON).content(content))
    	 .andExpect(status().isBadRequest())
    	 .andDo(print());
    }
     
    @Test
    public void testAddShouldReturn201Created() throws Exception
    {
    	String code = "ABC";
		Location location = new Location();
		location.setCode(code);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
		
		Mockito.when(service.add(any())).thenReturn(location);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
		mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON).content(bodyContent))
			//.andExpect(status().isCreated())
			//.andExpect(jsonPath("$.code", is(code)))
			//.andExpect(jsonPath("$.city_name", is("New York City")))
			//.andExpect(header().string("Location", END_POINT_PATH + "/" + code))
			//.andExpect(jsonPath("$._links.self.href", is("http://localhost" + END_POINT_PATH + "/" + code)))
			//.andExpect(jsonPath("$._links.realtime_weather.href", is("http://localhost/v1/realtime/" + code)))
			//.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + code)))
			//.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + code)))
			//.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + code)))			
			.andDo(print());	
    }
    
    @Test
    public void testValidateRequestBodyLocationCodeNotNull() throws Exception
    {
    	LocationDTO location = new LocationDTO();
    	
    	location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);	
		
		String bodyContent = mapper.writeValueAsString(location);
		mockMvc.perform(post(END_POINT_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error[0]", is("Location code cannot be null")))
		.andDo(print());	
    }
     
    @Test
	public void testValidateRequestBodyLocationCodeLength() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setCode("");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);	
		
		String bodyContent = mapper.writeValueAsString(location);
		
		mockMvc.perform(post(END_POINT_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error[0]", is("Location code must have 3-12 characters")))
			.andDo(print());			
	}	
    
    @Test
	public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setRegionName("");
		
		String bodyContent = mapper.writeValueAsString(location);
		
		MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
			.andExpect(status().isBadRequest())
            .andDo(print())
			.andReturn();		
		
		String responseBody = mvcResult.getResponse().getContentAsString();
		
		assertThat(responseBody).contains("Location code cannot be null");
		assertThat(responseBody).contains("City name cannot be null");
		assertThat(responseBody).contains("Region name must have 3-128 characters");
		assertThat(responseBody).contains("Country name cannot be null");
		assertThat(responseBody).contains("Country code cannot be null");
	}		
    
    @Test
    @Disabled
	public void testListShouldReturn204NoContent() throws Exception {
		Mockito.when(service.list()).thenReturn(Collections.emptyList());
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
    
    @Test
	@Disabled
	public void testListShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegionName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United States of America");
		location1.setEnabled(true);		
		
		Location location2 = new Location();
		location2.setCode("LACA_USA");
		location2.setCityName("Los Angeles");
		location2.setRegionName("California");
		location2.setCountryCode("US");
		location2.setCountryName("United States of America");
		location2.setEnabled(true);	
		
		Mockito.when(service.list()).thenReturn(List.of(location1, location2));
		
		mockMvc.perform(get(END_POINT_PATH))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].code", is("NYC_USA")))
			.andExpect(jsonPath("$[0].city_name", is("New York City")))
			.andExpect(jsonPath("$[1].code", is("LACA_USA")))
			.andExpect(jsonPath("$[1].city_name", is("Los Angeles")))			
			.andDo(print());			
	}
    
    @Test
    public void testListByPageShouldReturn204NoContent() throws Exception
    {
    	Mockito.when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(Page.empty());
    	
    	mockMvc.perform(get(END_POINT_PATH))
    	.andExpect(status().isNoContent())
    	.andDo(print());
    }
    
    @Test
	public void testListByPageShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegionName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United States of America");
		location1.setEnabled(true);		
		
		Location location2 = new Location();
		location2.setCode("LACA_USA");
		location2.setCityName("Los Angeles");
		location2.setRegionName("California");
		location2.setCountryCode("US");
		location2.setCountryName("United States of America");
		location2.setEnabled(true);	
		
		List<Location> locations = List.of(location1,location2);
		
		int pageSize = 5;
		int pageNum = 1;
		String sortField = "code";
		int totalElements = locations.size();
	
		Sort sort = Sort.by(sortField);
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        
		Page<Location> page = new PageImpl<>(locations, pageable, totalElements);
		
		Mockito.when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap()))
					.thenReturn(page);
		
        String requestURI = END_POINT_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
		
		mockMvc.perform(get(requestURI))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.locations[0].code", is("NYC_USA")))
			.andExpect(jsonPath("$._embedded.locations[0].city_name", is("New York City")))
			.andExpect(jsonPath("$._embedded.locations[1].code", is("LACA_USA")))
			.andExpect(jsonPath("$._embedded.locations[1].city_name", is("Los Angeles")))	
			.andExpect(jsonPath("$.page.size", is(pageSize)))
			.andExpect(jsonPath("$.page.number", is(pageNum)))
			.andExpect(jsonPath("$.page.total_elements", is(totalElements)))
			.andExpect(jsonPath("$.page.total_pages", is(1)))
			.andDo(print());	
    }
    
    @Test
	public void testDeleteShouldReturn404NotFound() throws Exception {
		String code = "LACA_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		
		LocationNotFoundException ex = new LocationNotFoundException(code);
		Mockito.doThrow(ex).when(service).delete(code);
		
		mockMvc.perform(delete(requestURI))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error[0]", is(ex.getMessage())))
			.andDo(print());
	}
    
    @Test
    public void testDeleteShouldReturn204NoContent() throws Exception
    {
    	String code = "LACA_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		
		Mockito.doNothing().when(service).delete(code);
		
		mockMvc.perform(delete(requestURI))
			.andExpect(status().isNoContent())
			.andDo(print());
    }
    
     
     
     
}
