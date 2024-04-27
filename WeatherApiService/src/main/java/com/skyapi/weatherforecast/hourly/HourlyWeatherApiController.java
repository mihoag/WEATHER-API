package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.BadRequestException;
import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/hourly")
@Validated
public class HourlyWeatherApiController {
	private HourlyWeatherService hourlyWeatherService;
	private GeolocationService locationService;
	private ModelMapper modelMapper;
	
	@Autowired
	public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService, GeolocationService locationService,
			ModelMapper modelMapper) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.locationService = locationService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping()
	public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);
		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
			Location locationFromIP = locationService.getLocation(ipAddress);
			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);
			if(hourlyForecast.isEmpty())
			{
				return ResponseEntity.noContent().build();
			}
            
		    HourlyWeatherListDTO dto = listEntity2DTO(hourlyForecast);
			
			return ResponseEntity.ok(addLinksByIP(dto));
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().build();
		}
		
	}
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyForecastByLocationCode(
			@PathVariable("locationCode") String locationCode, HttpServletRequest request) {
		
		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
			
			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
			
			if (hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			
			HourlyWeatherListDTO dto = listEntity2DTO(hourlyForecast);
			
			return ResponseEntity.ok(addLinksByLocation(dto, locationCode));
			
		} catch (NumberFormatException ex) {
			
			return ResponseEntity.badRequest().build();
			
		}
	}	
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String locationCode, 
			@RequestBody @Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException {
		
		if (listDTO.isEmpty()) {
			throw new BadRequestException("Hourly forecast data cannot be empty");
		}
		
		listDTO.forEach(System.out::println);
		
		List<HourlyWeather> listHourlyWeather = listDTO2ListEntity(listDTO);
		
		System.out.println();
		
		listHourlyWeather.forEach(System.out::println);
		
		List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, listHourlyWeather);
		
		HourlyWeatherListDTO updatedDto = listEntity2DTO(updateHourlyWeather);
		
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));
		
	}
    
	private HourlyWeatherListDTO addLinksByIP(HourlyWeatherListDTO dto) {
		
		dto.add(linkTo(
						methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
							.withSelfRel());
		
		dto.add(linkTo(
					methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null))
						.withRel("realtime_weather"));	
		return dto;
	}	
     
    private HourlyWeatherListDTO addLinksByLocation(HourlyWeatherListDTO dto, String locationCode) {
		
		dto.add(linkTo(
						methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
							.withSelfRel());
		
		dto.add(linkTo(
					methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode))
						.withRel("realtime_weather"));
		
		return dto;
	}	
	
	
	private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO) {
		List<HourlyWeather> listEntity = new ArrayList<>();
		
		listDTO.forEach(dto -> {
			HourlyWeather hw =modelMapper.map(dto, HourlyWeather.class);
			listEntity.add(hw);
		});
		
		return listEntity;
	}
	
	private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyForecast) {
		Location location = hourlyForecast.get(0).getId().getLocation();
		
		HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
		listDTO.setLocation(location.toString());
		
		hourlyForecast.forEach(hourlyWeather -> {
			HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
			listDTO.addWeatherHourlyDTO(dto);
		});
		
		return listDTO;
		
	}
	
	
	
	
    
}
