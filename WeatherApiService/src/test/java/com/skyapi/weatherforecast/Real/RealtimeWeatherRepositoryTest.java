package com.skyapi.weatherforecast.Real;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTest {
    
	@Autowired
	private RealtimeWeatherRepository repo;
	
	@Test
	public void TestGet()
	{
	   // RealtimeWeather r = repo.findByLocationCode("NYC_USA");
		RealtimeWeather r = repo.findByCountryCodeAndCity("US", "New York");
	    System.out.println(r);
	}
	
	
}
