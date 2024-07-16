package com.skyapi.weatherforecast.daily;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common_service.GeolocationService;
import com.skyapi.weatherforecast.exception_handler.BadRequestException;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherListDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherService;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/daily")
@Validated
public class DailyWeatherApiController {
	private DailyWeatherService dailyWeatherService;
	private GeolocationService locationService;
	private ModelMapper modelMapper;
	
	
	public DailyWeatherApiController(DailyWeatherService dailyWeatherService, GeolocationService locationService,
			ModelMapper modelMapper) {
		super();
		this.dailyWeatherService = dailyWeatherService;
		this.locationService = locationService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping()
	public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);
		try {
			Location locationFromIP = locationService.getLocation(ipAddress);
			List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);
			if(dailyForecast.isEmpty())
			{
				return ResponseEntity.noContent().build();
			}
            
            DailyWeatherListDTO dailyWeatherListDTO = listEntity2DTO(dailyForecast); 
			return ResponseEntity.ok(addLinksByIP(dailyWeatherListDTO));
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listDailyForecastByLocationCode(
			@PathVariable("locationCode") String locationCode) {
		
		try {
			List<DailyWeather> dailyForecast = dailyWeatherService.getByLocationCode(locationCode);
			
			if (dailyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			
			 DailyWeatherListDTO dailyWeatherListDTO = listEntity2DTO(dailyForecast); 
			
			return ResponseEntity.ok(addLinksByLocation(dailyWeatherListDTO, locationCode));
			
		} catch (NumberFormatException ex) {
			return ResponseEntity.badRequest().build();
		}
	}	
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateDailyForecast(@PathVariable("locationCode") String locationCode, 
			@RequestBody @Valid List<DailyWeatherDTO> listDTO) throws BadRequestException {
		
		if (listDTO.isEmpty()) {
			throw new BadRequestException("Daily forecast data cannot be empty");
		}
		
		List<DailyWeather> listDailyWeather = listDTO2ListEntity(listDTO);
		
		List<DailyWeather> updateDailyWeather = dailyWeatherService.updateByLocationCode(locationCode, listDailyWeather);
		
		DailyWeatherListDTO updatedDto = listEntity2DTO(updateDailyWeather);
		
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));
	}
    
	private DailyWeatherListDTO addLinksByIP(DailyWeatherListDTO dto) {
		
		dto.add(linkTo(
						methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null))
							.withSelfRel());
		
		
		dto.add(linkTo(
				methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
					.withRel("hourly_weather"));
		
		dto.add(linkTo(
					methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null))
						.withRel("realtime"));
		dto.add(linkTo(
				methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
					.withRel("full_forecast"));
		
		return dto;
	}	
     
    private DailyWeatherListDTO addLinksByLocation(DailyWeatherListDTO dto, String locationCode) {
		
    	dto.add(linkTo(
				methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
					.withSelfRel());
    	
		dto.add(linkTo(
						methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
							.withRel("hourly_forecast"));
		
		dto.add(linkTo(
				methodOn(FullWeatherApiController.class).getFullWeatherLocation(null))
					.withRel("full_forecast"));
		
		dto.add(linkTo(
					methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode))
						.withRel("realtime"));
		
		return dto;
	}	
	
	
	private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
		List<DailyWeather> listEntity = new ArrayList<>();
		
		listDTO.forEach(dto -> {
			DailyWeather dw =modelMapper.map(dto, DailyWeather.class);
			listEntity.add(dw);
		});
		
		return listEntity;
	}
	
	private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
		Location location = dailyForecast.get(0).getId().getLocation();
	    DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
		listDTO.setLocation(location.toString());
		
		dailyForecast.forEach(dailyWeather -> {
			DailyWeatherDTO dto = modelMapper.map(dailyWeather, DailyWeatherDTO.class);
			listDTO.addDailyWeatherDTO(dto);
		});
		
		return listDTO;
		
	}
}

