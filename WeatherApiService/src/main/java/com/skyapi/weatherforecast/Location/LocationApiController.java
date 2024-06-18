package com.skyapi.weatherforecast.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationApiController {
   
	private LocationService service;
	private ModelMapper modelMapper;
	
	public LocationApiController(LocationService service, ModelMapper modelMapper) {
		super();
		this.service = service;
		this.modelMapper = modelMapper;
	}
	@PostMapping
	public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO dto)
	{
		Location addedLocation = service.add(DTO2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
		return ResponseEntity.created(uri).body(addLinks2Item(entity2DTO(addedLocation)));
	}
	
	@GetMapping
	public ResponseEntity<?> listCollections()
	{
		List<Location> lsLocations = service.list();
		if(lsLocations.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		
		
		
		return ResponseEntity.ok(listEntity2DTO(lsLocations));
	}
	 
	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code)
	{
		Location location = service.getByCode(code);
		return ResponseEntity.ok(addLinks2Item(entity2DTO(location)));
	}
	
	@PutMapping
	public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto)
	{
		Location updatedLocation = service.update(DTO2Entity(dto));
		return ResponseEntity.ok(addLinks2Item(entity2DTO(updatedLocation)));
	}
	
	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code)
	{
		service.delete(code);
		return ResponseEntity.noContent().build();
	}
	
	public LocationDTO entity2DTO(Location location)
	{
	    return this.modelMapper.map(location,LocationDTO.class);	
	}
	
	public Location DTO2Entity(LocationDTO dto)
	{
		return this.modelMapper.map(dto, Location.class);
	}
	
	public List<LocationDTO> listEntity2DTO(List<Location> lsLocation)
	{
		List<LocationDTO> listDTO = new ArrayList<>();
		
		lsLocation.forEach(location -> {
		   listDTO.add(addLinks2Item(entity2DTO(location)));
		});
		return listDTO;
	}
	
	
	private LocationDTO addLinks2Item(LocationDTO dto)
	{
		dto.add(linkTo(
				methodOn(LocationApiController.class).getLocation(dto.getCode()))
					.withSelfRel());
		dto.add(linkTo(
				methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(dto.getCode()))
					.withRel("realtime"));	
		
		dto.add(linkTo(
				methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(dto.getCode(), null))
					.withRel("hourly_forecast"));
		dto.add(linkTo(
				methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(dto.getCode()))
					.withRel("daily_forecast"));
		return dto;
	}
}
