package com.skyapi.weatherforecast.daily;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, DailyWeatherId> {

	@Query(value = "select * from  weather_daily wd where wd.location_code in (select l.code from location l where l.code = ?1 and l.trashed = 0)", nativeQuery =  true)
	public List<DailyWeather> findByLocationCode(String locationCode);
	
	@Query(value = "select * from weather_daily wd where wd.location_code in (select l.code from location l where l.trashed = 0 and l.country_code = ?1 and l.city_name = ?2)" , nativeQuery = true)
	public Location findByCountryCodeAndCityName(String country, String cityName);
}
