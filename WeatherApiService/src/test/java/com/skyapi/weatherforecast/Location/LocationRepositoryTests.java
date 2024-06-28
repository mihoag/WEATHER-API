package com.skyapi.weatherforecast.Location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {
	@Autowired
	private LocationRepository repo;
	
   
	@Test
	public void testAddSuccess()
	{
	    Location location = new Location();
	    location.setCode("MBMH_IN");
		location.setCityName("Mumbai");
		location.setRegionName("Maharashtra");
		location.setCountryCode("IN");
		location.setCountryName("India");
		location.setEnabled(true);
		
		Location savedLocation = repo.save(location);
		assertThat(savedLocation).isNotNull();
		assertThat(savedLocation.getCode()).isEqualTo("MBMH_IN");
	}
	
	@Test
	@Disabled
	public void testListSuccess()
	{
		List<Location> locations = repo.findUntrashed();
		
		assertThat(locations).isNotEmpty();
		locations.forEach(System.out::println);
	}
	

	@Test
	public void testListFirstPage()
	{
		int pageNum = 0;
		int pageSize = 5;
		
	    Pageable pageLocation =  PageRequest.of(pageNum, pageSize);
	    Page<Location> locationPage = repo.findUntrashed(pageLocation);
	    
	    assertThat(locationPage.getSize()).isEqualTo(pageSize);
	    locationPage.forEach(System.out::println);
	}
	
	@Test
	public void testListPageNoContent()
	{

		int pageNum = 100;
		int pageSize = 5;
		
	    Pageable pageLocation =  PageRequest.of(pageNum, pageSize);
	    Page<Location> locationPage = repo.findUntrashed(pageLocation);
	    
	    assertThat(locationPage).isEmpty();
	}
	

	@Test
	public void testList2ndPage()
	{
		int pageNum = 0;
		int pageSize = 5;
		Sort sort = Sort.by("code").descending();
		
	    Pageable pageLocation =  PageRequest.of(pageNum, pageSize, sort);
	    Page<Location> page = repo.findUntrashed(pageLocation);
	    
	    assertThat(page.getSize()).isEqualTo(pageSize);
	    page.forEach(System.out::println);
	}
	
	
	@Test
	public void testGetFound()
	{
		String code = "BJG_CN";
		
		Location location = repo.get(code);
		
		assertThat(location).isNotNull();
		assertThat(location.getCode()).isEqualTo(code);
	}
	
	@Test
	public void testGetNotFound()
	{
		String code = "ABCDE";
		
		Location location = repo.get(code);
		
		assertThat(location).isNull();
	}
	
	@Test
	public void testTrashSuccess()
	{
		String code = "LACA_US";
		repo.trashByCode(code);
		Location location = repo.get(code);
		assertThat(location).isNull();
	}
	
	@Test
	public void testAddRealtimeWeatherData()
	{
		String code = "BJG_CN";
		Location location = repo.get(code);
		RealtimeWeather realtime = location.getRealtimeWeather();
		if(realtime == null)
		{
			realtime = new RealtimeWeather();
			realtime.setLocation(location);
			location.setRealtimeWeather(realtime);
		}
		
		realtime.setTemperature(-1);
		realtime.setHumidity(30);
		realtime.setPrecipitation(40);
		realtime.setStatus("Snowy");
		realtime.setWindSpeed(15);
		realtime.setLastUpdated(new Date());
		
		Location updatedLocation = repo.save(location);
	    assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
	}
	
	@Test
	public void testAddHourlyWeatherData()
	{
		String code = "MBMH_IN";
		
		Location location = repo.get(code);
		List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();
		
		HourlyWeather forecast1 = new HourlyWeather().id(location, 10)
				.temperature(15)
				.precipitation(40)
				.status("Sunny");
		HourlyWeather forecast2 = new HourlyWeather().id(location, 11)
				.temperature(16)
				.precipitation(50)
				.status("Cloudy");
		listHourlyWeather.add(forecast1);
		listHourlyWeather.add(forecast2);
		
		Location updatedLocation = repo.save(location);
		
		assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
	}
	
	@Test
	public void  testFindByCountryAndCityNameNotFound()
	{
		String countryCode = "ABC";
		String cityName = "New York City";
        Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);	
		assertThat(location).isNull();
	}
	
	@Test
	public void  testFindByCountryAndCityNameFound()
	{
		String countryCode = "IN";
		String cityName = "Mumbai";
        Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		assertThat(location).isNotNull();
		assertThat(location.getCountryCode()).isEqualTo(countryCode);
		assertThat(location.getCityName()).isEqualTo(cityName);
	}
	
	@Test
	public void testAddDailyWeatherData() {
		Location location = repo.findById("LACA_US").get();
		
		List<DailyWeather> listDailyWeather = location.getListDailyWeather();
		
		DailyWeather forecast1 = new DailyWeather()
				.Location(location)
				.DayOfMonth(16)
				.Month(7)
				.MinTemp(25)
				.MaxTemp(33)
				.precipitation(20)
				.Status("Sunny");
		
		DailyWeather forecast2 = new DailyWeather()
				.Location(location)
				.DayOfMonth(17)
				.Month(7)
				.MinTemp(26)
				.MaxTemp(34)
				.precipitation(10)
				.Status("Clear");	
		
		listDailyWeather.add(forecast1);
		listDailyWeather.add(forecast2);
		
		Location updatedLocation = repo.save(location);
		
		assertThat(updatedLocation.getListDailyWeather()).isNotEmpty();
	}
	

	
	
}
