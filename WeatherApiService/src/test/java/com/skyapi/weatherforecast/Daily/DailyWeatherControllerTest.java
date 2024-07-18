package com.skyapi.weatherforecast.Daily;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common_service.GeolocationService;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.daily.DailyWeatherService;
import com.skyapi.weatherforecast.exception_handler.GeolocationException;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;

@WebMvcTest(DailyWeatherApiController.class)
public class DailyWeatherControllerTest {
	
	private static final String END_POINT_PATH = "/v1/daily";
	private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";	
	
	@Autowired private MockMvc mockMvc;
	
	@Autowired private ObjectMapper objectMapper;
    
	@MockBean
	private GeolocationService locationSer;
	
	@MockBean
	private DailyWeatherService dailySer;
	
	@MockBean
	private ModelMapper modelMapper;
	
	
	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception
	{
		GeolocationException ex = new GeolocationException("Geolocation error");
 		Mockito.when(locationSer.getLocation(anyString())).thenThrow(ex);
 		mockMvc.perform(get(END_POINT_PATH))
 		.andExpect(status().isBadRequest())
 		.andExpect(jsonPath("$.error[0]", is(ex.getMessage())))
 		.andDo(print());
	}
	
	@Test
	public void testGetByIPShouldReturn404NotFound() throws Exception
	{
		String countryCode = "ABC";
		String cityname = "XYZ";
		
	    Location location = new Location().countryCode(countryCode).cityName(cityname);
	    
	    Mockito.when(locationSer.getLocation(anyString())).thenReturn(location);
	    LocationNotFoundException ex  = new LocationNotFoundException(countryCode, cityname);
	   
	    Mockito.when(dailySer.getByLocation(location)).thenThrow(ex);
	    
	    mockMvc.perform(get(END_POINT_PATH))
	    .andExpect(status().isNotFound())
	    .andExpect(jsonPath("$.error[0]", is(ex.getMessage())))
	    .andDo(print());
	}
	@Test
	public void testGetByIPShouldReturn204NoContent() throws Exception {
	     String location = "NYC_USA";
	     List<DailyWeather> ls = new ArrayList<>();
	     
	     Location locationDb = new Location().code(location);
	     Mockito.when(locationSer.getLocation(anyString())).thenReturn(locationDb);
	     Mockito.when(dailySer.getByLocation(locationDb)).thenReturn(ls);
	     
	     mockMvc.perform(get(END_POINT_PATH))
	     .andExpect(status().isNoContent())
	     .andDo(print());
	    
	}
	@Test
	public void testGetByIPShouldReturn200OK() throws Exception
	{
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		
		DailyWeather forecast1 = new DailyWeather()
				.Location(location)
				.DayOfMonth(16)
				.Month(7)
				.MinTemp(23)
				.MaxTemp(32)
				.precipitation(40)
				.Status("Cloudy");
		
		DailyWeather forecast2 = new DailyWeather()
				.Location(location)
				.DayOfMonth(17)
				.Month(7)
				.MinTemp(25)
				.MaxTemp(34)
				.precipitation(30)
				.Status("Sunny");		
		
		Mockito.when(locationSer.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(dailySer.getByLocation(location)).thenReturn(List.of(forecast1, forecast2));
		
	
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isOk())			
				.andDo(print());
	}
	
	@Test
	public void testGetByCodeShouldReturn404NotFound() {
		 
	}
	
	@Test
	public void testGetByCodeShouldReturn204NoContent()
	{
		
	}
	
	@Test
	public void testGetByCodeShouldReturn200OK() {
		
	}
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseNoData() {
		
	}
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidData() {
		
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() {
		
	}
	
	@Test
	public void testUpdateShouldReturn200OK() {
		
	}
}
