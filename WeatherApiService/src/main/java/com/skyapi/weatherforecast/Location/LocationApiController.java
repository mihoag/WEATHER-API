package com.skyapi.weatherforecast.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.exception_handler.BadRequestException;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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
	
	private Map<String, String> propertyMap = Map.of(
			"code", "code",
			"city_name", "cityName",
			"region_name", "regionName",
			"country_code", "countryCode",
			"country_name", "countryName",
			"enabled", "enabled"
		);
	
	@PostMapping
	public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO dto)
	{
		
		//System.out.println(dto.getCode() +  " " + dto.getCityName() + " " + dto.getCountryCode() + " " + dto.getCountryName() + " " + dto.getRegionName());
		Location location = DTO2Entity(dto);
		
		System.out.println(location.getCode()+ " " + location.getCityName() + " " + location.getCountryCode() + " " + location.getCountryName() + " " + location.getRegionName());
		Location addedLocation = service.add(location);
		
		System.out.println(addedLocation.getCode()+ " " + addedLocation.getCityName() + " " + addedLocation.getCountryCode() + " " + addedLocation.getCountryName() + " " + addedLocation.getRegionName());
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
		return ResponseEntity.created(uri).body(addLinks2Item(entity2DTO(addedLocation)));
	}
	
	
	@Deprecated
	ResponseEntity<?> listLocations()
	{
		List<Location> lsLocations = service.list();
		if(lsLocations.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2DTO(lsLocations));
	}
	
	@GetMapping 
	public ResponseEntity<?> listLocations(
			@RequestParam(value = "page", required = false, defaultValue = "1") 
			@Min(value = 1)	Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "5") 
			@Min(value = 2) @Max(value = 20) Integer pageSize,
            @RequestParam(value = "sort", required = false, defaultValue = "code") String sortOption,
            @RequestParam(value = "enabled", required = false, defaultValue = "") String enabled,
            @RequestParam(value = "region_name", required = false, defaultValue = "") String regionName,
            @RequestParam(value = "country_code", required = false, defaultValue = "") String countryCode
			) throws BadRequestException
	{
		sortOption = validateSortOption(sortOption);
	    Map<String, Object> filterFields = getFilterFields(enabled, regionName, countryCode);		
		Page<Location> page = service.listByPage(pageNum - 1, pageSize, sortOption, filterFields);
		List<Location> lsLocations = page.getContent();
		
		//System.out.println(lsLocations.size());
		if(lsLocations.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		
		List<LocationDTO> locationDtos = listEntity2DTO(lsLocations);
		return ResponseEntity.ok(addPageMetadataAndLinks2Collection(locationDtos, page, sortOption, enabled, regionName, countryCode));
	}
	
	private Map<String, Object> getFilterFields(String enabled, String regionName, String countryCode) {
		Map<String, Object> filterFields = new HashMap<>();
		
		if (!"".equals(enabled)) {
			filterFields.put("enabled", Boolean.parseBoolean(enabled));
		}
		
		if (!"".equals(regionName)) {
			filterFields.put("regionName", regionName);
		}		
		
		if (!"".equals(countryCode)) {
			filterFields.put("countryCode", countryCode);
		}
		return filterFields;
	}
	
	private String validateSortOption(String sortOption) throws BadRequestException {
		String translatedSortOption = sortOption;
		
		String[] sortFields = sortOption.split(",");
		
		
		if (sortFields.length > 1) { // sorted by multiple fields
			
			for (int i = 0; i < sortFields.length; i++) {
				String actualFieldName = sortFields[i].replace("-", "");
				
				if (!propertyMap.containsKey(actualFieldName)) {
					throw new BadRequestException("invalid sort field: " + actualFieldName);
				}
				
				translatedSortOption = translatedSortOption.replace(actualFieldName, propertyMap.get(actualFieldName));
			}
			
		} else { // sorted by a single field
			String actualFieldName = sortOption.replace("-", "");
			if (!propertyMap.containsKey(actualFieldName)) {
				throw new BadRequestException("invalid sort field: " + actualFieldName);
			}
			
			translatedSortOption = translatedSortOption.replace(actualFieldName, propertyMap.get(actualFieldName));
		}
		return translatedSortOption;
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
	
	
	private CollectionModel<LocationDTO> addPageMetadataAndLinks2Collection(
			List<LocationDTO> listDTO, Page<Location> pageInfo, String sortField,
			String enabled, String regionName, String countryCode) throws BadRequestException {
		
		String actualEnabled = "".equals(enabled) ? null : enabled;
		String actualRegionName = "".equals(regionName) ? null : regionName;
		String actualCountryCode = "".equals(countryCode) ? null : countryCode;
		
				 
		// add self link to each individual item
		for (LocationDTO dto : listDTO) {
			dto.add(linkTo(methodOn(LocationApiController.class).getLocation(dto.getCode())).withSelfRel());
		}
		
		int pageSize = pageInfo.getSize();
		int pageNum = pageInfo.getNumber() + 1;
		long totalElements = pageInfo.getTotalElements();
		int totalPages = pageInfo.getTotalPages();
		
		PageMetadata pageMetadata = new PageMetadata(pageSize, pageNum, totalElements);
		
		CollectionModel<LocationDTO> collectionModel = PagedModel.of(listDTO, pageMetadata);
		
		// add self link to collection
		collectionModel.add(linkTo(methodOn(LocationApiController.class)
								.listLocations(pageNum, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
									.withSelfRel());
		
		if (pageNum > 1) {
			// add link to first page if the current page is not the first one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class)
							.listLocations(1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
								.withRel(IanaLinkRelations.FIRST));
			
			// add link to the previous page if the current page is not the first one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class)
							.listLocations(pageNum - 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
								.withRel(IanaLinkRelations.PREV));			
		}	
		
		if (pageNum < totalPages) {
			// add link to next page if the current page is not the last one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class)
							.listLocations(pageNum + 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
								.withRel(IanaLinkRelations.NEXT));			
			
			// add link to last page if the current page is not the last one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class)
							.listLocations(totalPages, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
								.withRel(IanaLinkRelations.LAST));					
		}
		
		
		return collectionModel;
		
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
		   listDTO.add(entity2DTO(location));
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
