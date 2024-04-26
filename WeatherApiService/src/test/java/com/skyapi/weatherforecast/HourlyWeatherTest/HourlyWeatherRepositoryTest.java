package com.skyapi.weatherforecast.HourlyWeatherTest;

import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.hourly.HourlyWeatherRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTest {
    
	@Autowired
	private HourlyWeatherRepository repo;
	
	
	@Test
	public void testGet()
	{
	   	List<HourlyWeather> ls = repo.findByLocationCode("NYC_USA", 2);
	   	for(HourlyWeather h : ls)
	   	{
	   		System.out.println(h);
	   	}
	}
	
}
