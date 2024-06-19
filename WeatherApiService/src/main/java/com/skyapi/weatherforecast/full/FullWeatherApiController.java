package com.skyapi.weatherforecast.full;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common_service.GeolocationService;
import com.skyapi.weatherforecast.exception_handler.BadRequestException;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {

	private GeolocationService locationService;
	private FullWeatherService weatherService;
	private ModelMapper modelMapper;
	
	public FullWeatherApiController(GeolocationService locationService, FullWeatherService weatherService,
			ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.weatherService = weatherService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping
	public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request)
	{
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		Location locationInDB = weatherService.getByLocation(locationFromIP);
		
        FullWeatherDTO dto = entity2DTO(locationInDB);
		return ResponseEntity.ok(dto);
	}
	
	
	@GetMapping("/{location_code}")
	public ResponseEntity<?> getFullWeatherByIPAddress(@PathVariable(name = "location_code") String code)
	{
		
		Location locationInDB = weatherService.getByCode(code);
		
        FullWeatherDTO dto = entity2DTO(locationInDB);
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping("/{location_code}")
	public ResponseEntity<?> updateFullWeatherByLocationCode(@PathVariable(name = "location_code") String code, @RequestBody @Valid FullWeatherDTO dto) throws BadRequestException
	{
		if (dto.getListHourlyWeather().isEmpty()) {
			throw new BadRequestException("Hourly weather data cannot be empty");
		}
		
		if (dto.getListDailyWeather().isEmpty()) {
			throw new BadRequestException("Daily weather data cannot be empty");
		}
		Location requestLocation = dto2Entity(dto);
	    
		Location updatedLocation = weatherService.update(code, requestLocation);
		
		FullWeatherDTO updatedDto = entity2DTO(updatedLocation);
		return ResponseEntity.ok(updatedDto);
	}
		
	private FullWeatherDTO entity2DTO(Location entity) {
		FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
		// do not show the field location in realtime_weather object
		dto.getRealtimeWeather().setLocation(null);
		return dto;
	}
	
	private Location dto2Entity(FullWeatherDTO dto) {
		return modelMapper.map(dto, Location.class);
	}
}
