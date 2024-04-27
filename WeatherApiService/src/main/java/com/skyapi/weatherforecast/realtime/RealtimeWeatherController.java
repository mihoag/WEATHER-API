package com.skyapi.weatherforecast.realtime;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherController {
	private GeolocationService locationService;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;
	public RealtimeWeatherController(GeolocationService locationService, RealtimeWeatherService realtimeWeatherService,
			ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}
	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
		
		RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
		
		return ResponseEntity.ok(addLinksByIP(dto));
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
		RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
		
		RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);
		
		return ResponseEntity.ok(addLinksByLocation(dto, locationCode));
	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
			@RequestBody @Valid RealtimeWeatherDTO dto) {
		
		RealtimeWeather realtimeWeather = dto2Entity(dto);
		realtimeWeather.setLocationCode(locationCode);
		
		RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
		
		RealtimeWeatherDTO updatedDto = entity2DTO(updatedRealtimeWeather);
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));
	}
	
	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
	}
	
	private RealtimeWeather dto2Entity(RealtimeWeatherDTO dto) {
		return modelMapper.map(dto, RealtimeWeather.class);
	}
	
	private RealtimeWeatherDTO addLinksByIP(RealtimeWeatherDTO dto) {
		
		dto.add(linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null)).withSelfRel());
		return dto;
	}
	
	private RealtimeWeatherDTO addLinksByLocation(RealtimeWeatherDTO dto, String locationCode) {
		
		dto.add(linkTo(
						methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode))
							.withSelfRel());
		
		dto.add(linkTo(
				methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
					.withRel("hourly_forecast"));
		return dto;
	}	
	
}
