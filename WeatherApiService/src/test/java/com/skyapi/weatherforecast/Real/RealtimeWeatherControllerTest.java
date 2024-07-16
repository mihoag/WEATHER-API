package com.skyapi.weatherforecast.Real;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.common_service.GeolocationService;
import com.skyapi.weatherforecast.exception_handler.GeolocationException;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherService;

@WebMvcTest(RealtimeWeatherController.class)
public class RealtimeWeatherControllerTest {
	 private static final String END_POINT_PATH = "/v1/realtime";
     private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
 	 private static final String REQUEST_CONTENT_TYPE = "application/json";	
 	 
 	 @Autowired
 	 private MockMvc mockMvc;
 	 
 	 @MockBean
 	 private RealtimeWeatherService realtimeSer;
 	 
 	 @MockBean
 	 private ModelMapper modelMapper;
 	 
 	 @MockBean
 	 private GeolocationService locationSer;
 	 
 	 
 	 
 	 @Test
 	 public void testGetShouldReturnStatus400BadRequest() throws Exception
 	 {
 		GeolocationException ex = new GeolocationException("Geolocation error");
 		Mockito.when(locationSer.getLocation(anyString())).thenThrow(ex);
 		mockMvc.perform(get(END_POINT_PATH))
 		.andExpect(status().isBadRequest())
 		.andExpect(jsonPath("$.error[0]", is(ex.getMessage())))
 		.andDo(print());
 	 }
 	 
 	 
 	 @Test
 	 public void testGetShouldReturnStatus404NotFound() throws Exception
 	 {
 		 Location location = new Location();
 		 location.setCountryCode("US");
 		 location.setCityName("ABC");
 		 
 		 Mockito.when(locationSer.getLocation(anyString())).thenReturn(location);
 		
 		 
 		 LocationNotFoundException ex = new LocationNotFoundException(location.getCountryCode(), location.getCityName());
 		 Mockito.when(realtimeSer.getByLocation(location)).thenThrow(ex);
 		 
 		 mockMvc.perform(get(END_POINT_PATH))
 		 .andExpect(status().isNotFound())
 		 .andExpect(jsonPath("$.error[0]", is(ex.getMessage())))
 		 .andDo(print());
 	 }
 	 
 	 @Test
 	 public void testGetShouldReturnStatus200OK() throws Exception
 	 {
 		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityName("San Franciso");
		location.setRegionName("California");
		location.setCountryName("United States of America");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		
		Mockito.when(locationSer.getLocation(anyString())).thenReturn(location);
		Mockito.when(realtimeSer.getByLocation(location)).thenReturn(realtimeWeather);
		
		
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime")))
		.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly")))
		.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily")))
		.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full")))
		.andDo(print());		
 	 }
 	 
 	 public void testGetByLocationCodeShouldReturnStatus404NotFound()
 	 {
 		 
 	 }
 	 
 	 public void testGetByLocationCodeShouldReturnStatus200OK()
 	 {
 		 
 	 }
 	 
 	 public void testUpdateShouldReturn400BadRequest()
 	 {
 		 
 	 }
 	 
 	 public void testUpdateShouldReturn404NotFound()
 	 {
 		 
 	 }
 	 
 	 public void testUpdateShouldReturn200OK()
 	 {
 		 
 	 }

}
