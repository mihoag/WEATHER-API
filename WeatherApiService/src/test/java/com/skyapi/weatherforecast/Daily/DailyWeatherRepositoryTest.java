package com.skyapi.weatherforecast.Daily;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherRepository;
import com.skyapi.weatherforecast.location.LocationRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DailyWeatherRepositoryTest {
    
	@Autowired private LocationRepository locationRepo;
	@Autowired private DailyWeatherRepository dailyRepo;
	
	@Test
	public void testAdd() {
		String locationCode = "NYC_USA";
		Location location = locationRepo.findById(locationCode).get();
		
		DailyWeather dw = new DailyWeather().Location(location)
				.DayOfMonth(16)
				.Month(7)
				.MinTemp(23)
				.MaxTemp(32)
				.precipitation(40)
				.Status("Cloudy");
	   DailyWeather updateDailyWeather = dailyRepo.save(dw);
	   assertThat(dw).isNotNull();
	   assertThat(dw.getId().getLocation().getCode()).isEqualTo(locationCode);
	}
	
	@Test
	public void testDelete() {
		int month = 7;
		int day = 16;
		String location = "NYC_USA";
	    
		Location locationDb = locationRepo.get(location);
	
	    DailyWeatherId id = new DailyWeatherId(day,month,locationDb);
	    
	    DailyWeather dw = dailyRepo.findById(id).get();
	    dailyRepo.delete(dw);
        
	    Optional<DailyWeather> result = dailyRepo.findById(id);	
		assertThat(result).isNotPresent();
	}
	
	@Test
	public void testFindByLocationCodeFound() {
	   String code = "NYC_USA";
	   List<DailyWeather> lsDailyWeather = dailyRepo.findByLocationCode(code);
	   
	   assertThat(lsDailyWeather).isNotEmpty();
	}
	
	@Test
	public void testFindByLocationCodeNotFound() {
		 String code = "ABC";
		 List<DailyWeather> lsDailyWeather = dailyRepo.findByLocationCode(code);  
		 assertThat(lsDailyWeather).isEmpty();
	}
	
}
