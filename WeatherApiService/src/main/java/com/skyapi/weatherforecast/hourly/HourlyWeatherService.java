package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.Location.LocationNotFoundException;
import com.skyapi.weatherforecast.Location.LocationRepository;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;

@Service
public class HourlyWeatherService {
   @Autowired
   private HourlyWeatherRepository hourlyWeatherRepo;
   
   @Autowired
   private LocationRepository locationRepo;
   
   public List<HourlyWeather> getByLocation(Location location, int currentHour) {
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}	
		return hourlyWeatherRepo.findByLocationCode(locationInDB.getCode(), currentHour);
	}
   
   public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) {
		
		Location locationInDB = locationRepo.get(locationCode);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);
	}
   
   public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeatherInRequest) {
		Location location = locationRepo.get(locationCode);
		
		if (location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		for (HourlyWeather item : hourlyWeatherInRequest) {
			item.getId().setLocation(location);
		}
		
		List<HourlyWeather> hourlyWeatherInDB = location.getListHourlyWeather();
		List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();
		
		for (HourlyWeather item : hourlyWeatherInDB) {
			if (!hourlyWeatherInRequest.contains(item)) {
				hourlyWeatherToBeRemoved.add(item.getShallowCopy());
			}
		}
		
		for (HourlyWeather item : hourlyWeatherToBeRemoved) {
			hourlyWeatherInDB.remove(item);
		}
		
		return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherInRequest);
	}
   
   
}
