package com.skyapi.weatherforecast.Real;

import static org.assertj.core.api.Assertions.assertThat;

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
	public void testUpdate()
	{
       String locationCode = "NYC_USA";
       RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
       
       realtimeWeather.setHumidity(40);
       
       RealtimeWeather updateRealtimeWeather = repo.save(realtimeWeather);
       assertThat(updateRealtimeWeather.getHumidity()).isEqualTo(40);
	}
	
	@Test
	public void testFindByCountryCodeAndCityNotFound()
	{
		 String countryCode = "ABC";
		 String city = "XYZ";
		 
		 RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, city);
		 assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByCountryCodeAndCityFound()
	{
		 String countryCode = "US";
		 String city = "New YorK City";
		 
		 RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, city);
		 assertThat(realtimeWeather).isNotNull();
	}
	
	@Test
	public void testFindByLocationNotFound()
	{
		String locationCode = "USA";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByTrashedLocationNotFound()
	{
	   String locationCode = "LACA_US";
	   RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
	   assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByLocationFound()
	{
		String locationCode = "NYC_USA";
	   RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
	   assertThat(realtimeWeather).isNotNull();
	}
}
