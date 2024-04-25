package com.skyapi.weatherforecast.realtime;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.Location.LocationNotFoundException;
import com.skyapi.weatherforecast.Location.LocationRepository;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

@Service
public class RealtimeWeatherService {
   private RealtimeWeatherRepository realtimeWeatherRepo;
   private LocationRepository  locationRepo;
   public RealtimeWeatherService() {
	// TODO Auto-generated constructor stub
   }
   @Autowired
   public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepo, LocationRepository locationRepo) {
	 this.realtimeWeatherRepo = realtimeWeatherRepo;
	 this.locationRepo = locationRepo;
   }
   
   public RealtimeWeather getByLocation(Location location)
   {
	   String countryCode = location.getCountryCode();
	   String cityName = location.getCityName();
	   
	   RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);
	   
	   if (realtimeWeather == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		return realtimeWeather;
   }
	public RealtimeWeather getByLocationCode(String locationCode) {
		RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode);
		
		if (realtimeWeather == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		return realtimeWeather;
	}
   
	public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) {
		Location location = locationRepo.get(locationCode);
		
		if (location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		realtimeWeather.setLocation(location);
		realtimeWeather.setLastUpdated(new Date());
		
		if (location.getRealtimeWeather() == null) {
			location.setRealtimeWeather(realtimeWeather);
			Location updatedLocation = locationRepo.save(location);
			
			return updatedLocation.getRealtimeWeather();
		}
		
		return realtimeWeatherRepo.save(realtimeWeather);
	}
   
}