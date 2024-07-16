package com.skyapi.weatherforecast.base;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MainController.class)
public class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private static final String BASE_URI = "/";
	
	@Test
	public void testMainController() throws Exception
	{
		mockMvc.perform(get(BASE_URI))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.locationsUrl", is("http://localhost/v1/locations{?page,size,sort,enabled,region_name,country_code}")))
		.andExpect(jsonPath("$.locationByCodeUrl", is("http://localhost/v1/locations/{code}")))
		.andExpect(jsonPath("$.realtimeWeatherByIpUrl", is("http://localhost/v1/realtime")))
		.andExpect(jsonPath("$.realtimeWeatherByCodeUrl", is("http://localhost/v1/realtime/{locationCode}")))
		.andExpect(jsonPath("$.hourlyForecastByIpUrl", is("http://localhost/v1/hourly")))
		.andExpect(jsonPath("$.hourlyForecastByCodeUrl", is("http://localhost/v1/hourly/{locationCode}")))
		.andExpect(jsonPath("$.dailyForecastByIpUrl", is("http://localhost/v1/daily")))
		.andExpect(jsonPath("$.dailyForecastByCodeUrl", is("http://localhost/v1/daily/{locationCode}")))
		.andExpect(jsonPath("$.fullWeatherByIpUrl", is("http://localhost/v1/full")))
		.andExpect(jsonPath("$.fullWeatherByCodeUrl", is("http://localhost/v1/full/{location_code}")))
		.andDo(print());	
	}
}
