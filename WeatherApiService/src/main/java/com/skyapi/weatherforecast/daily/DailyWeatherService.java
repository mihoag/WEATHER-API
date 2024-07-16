package com.skyapi.weatherforecast.daily;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class DailyWeatherService {
     @Autowired
     private DailyWeatherRepository dailyWeatherRepo;
     
     @Autowired
     private LocationRepository locationRepo;
     
     public List<DailyWeather> getByLocation(Location location) {
 		String countryCode = location.getCountryCode();
 		String cityName = location.getCityName();
 		
 		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
 		
 		if (locationInDB == null) {
 			throw new LocationNotFoundException(countryCode, cityName);
 		}	
 		return dailyWeatherRepo.findByLocationCode(locationInDB.getCode());
 	}
    
    public List<DailyWeather> getByLocationCode(String locationCode) {
 		
 		Location locationInDB = locationRepo.get(locationCode);
 		
 		if (locationInDB == null) {
 			throw new LocationNotFoundException(locationCode);
 		}
 		
 		return dailyWeatherRepo.findByLocationCode(locationInDB.getCode());
 	}
    
    public List<DailyWeather> updateByLocationCode(String locationCode, List<DailyWeather> dailyWeatherInRequest) {
 		Location location = locationRepo.get(locationCode);
 		
 		if (location == null) {
 			throw new LocationNotFoundException(locationCode);
 		}
 		
 		for (DailyWeather item : dailyWeatherInRequest) {
 			item.getId().setLocation(location);
 		}
 		
 		List<DailyWeather> dailyWeatherInDB = location.getListDailyWeather();
 		List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList<>();
 		
 		for (DailyWeather item : dailyWeatherInDB) {
 			if (!dailyWeatherInRequest.contains(item)) {
 				dailyWeatherToBeRemoved.add(item.getShallowCopy());
 			}
 		}
 		
 		for (DailyWeather item : dailyWeatherToBeRemoved) {
 			dailyWeatherInDB.remove(item);
 		}
 		
 		return (List<DailyWeather>) dailyWeatherRepo.saveAll(dailyWeatherInRequest);
 	}
    
    
}
