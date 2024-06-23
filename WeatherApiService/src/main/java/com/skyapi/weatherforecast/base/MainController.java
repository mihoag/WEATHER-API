package com.skyapi.weatherforecast.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforecast.location.LocationApiController;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class MainController {
      
	@GetMapping("/")
	public ResponseEntity<?> getBaseUrl()
	{
		RootEntity rootEntity = new RootEntity();
		
		String locationsUrl = linkTo(methodOn(LocationApiController.class).listLocations(null, null, null, null, null, null)).toString();
		rootEntity.setLocationsUrl(locationsUrl);
		
		String locationByCodeUrl = linkTo(methodOn(LocationApiController.class).getLocation(null)).toString();
		rootEntity.setLocationByCodeUrl(locationByCodeUrl);
		
		String realtimeWeatherByIpUrl = linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null)).toString();
		rootEntity.setRealtimeWeatherByIpUrl(realtimeWeatherByIpUrl);
		
		String realtimeWeatherByCodeUrl = linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(null)).toString();
		rootEntity.setRealtimeWeatherByCodeUrl(realtimeWeatherByCodeUrl);
		
		String hourlyForecastByIpUrl = linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).toString();
		rootEntity.setHourlyForecastByIpUrl(hourlyForecastByIpUrl);
		
		String hourlyForecastByCodeUrl = linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(null, null)).toString();
		rootEntity.setHourlyForecastByCodeUrl(hourlyForecastByCodeUrl);
		
		String dailyForecastByIpUrl = linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).toString();
		rootEntity.setDailyForecastByIpUrl(dailyForecastByIpUrl);
		
		String dailyForecastByCodeUrl = linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(null)).toString();
		rootEntity.setDailyForecastByCodeUrl(dailyForecastByCodeUrl);
		
		String fullWeatherByIpUrl = linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).toString();
		rootEntity.setFullWeatherByIpUrl(fullWeatherByIpUrl);
		
		String fullWeatherByCodeUrl = linkTo(methodOn(FullWeatherApiController.class).getFullWeatherLocation(null)).toString();
		rootEntity.setFullWeatherByCodeUrl(fullWeatherByCodeUrl);
		
		return ResponseEntity.ok(rootEntity);
	}
}
