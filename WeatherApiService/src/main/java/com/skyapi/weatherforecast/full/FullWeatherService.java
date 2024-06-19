package com.skyapi.weatherforecast.full;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class FullWeatherService extends AbstractLocationService {

	public FullWeatherService(LocationRepository repo) {
		this.repo = repo;
	}
	
	public Location getByLocation(Location locationFromIP) {
		String cityName = locationFromIP.getCityName();
		String countryCode = locationFromIP.getCountryCode();
		
		Location locationInDB = repo.findByCountryCodeAndCityName(countryCode, cityName);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		return locationInDB;
	}

	public Location update(String locationCode, Location locationInRequest) {
		Location locationInDB = repo.get(locationCode);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		
		
		setLocationForWeatherData(locationInRequest, locationInDB);
		
		saveRealtimeWeatherIfNotExistBefore(locationInRequest, locationInDB);
		
		locationInRequest.copyAllFieldsFrom(locationInDB);
		
		//System.out.println(locationInRequest.getRealtimeWeather());
		return repo.save(locationInRequest);
	}

	private void setLocationForWeatherData(Location locationInRequest, Location locationInDB)
	{
	   RealtimeWeather realTimeWeather = locationInRequest.getRealtimeWeather();
	   realTimeWeather.setLocation(locationInDB);
	   realTimeWeather.setLocationCode(locationInDB.getCode());
	   realTimeWeather.setLastUpdated(new Date());
	   
	   List<HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather();
	   listHourlyWeather.forEach(hw -> hw.getId().setLocation(locationInDB));
	   
	   List<DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather();
	   listDailyWeather.forEach(dw -> dw.getId().setLocation(locationInDB));
	}
	
	private void saveRealtimeWeatherIfNotExistBefore(Location locationInRequest, Location locationInDB)
	{
		if(locationInDB.getRealtimeWeather() == null)
		{
		    locationInDB.setRealtimeWeather(locationInRequest.getRealtimeWeather());
		    repo.save(locationInDB);
		}
	}
	
	
	
	
}
