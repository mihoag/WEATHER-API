package com.skyapi.weatherforecast.daily;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


public class DailyWeatherListDTO extends RepresentationModel<DailyWeatherListDTO> {
	private String location;
	
	@JsonProperty("daily_forecast")
	private List<DailyWeatherDTO> dailyWeatherlistDTO = new ArrayList<>();

	public DailyWeatherListDTO()
	{	
	}
	
	public DailyWeatherListDTO(String location, List<DailyWeatherDTO> dailyWeatherlistDTO) {
		super();
		this.location = location;
		this.dailyWeatherlistDTO = dailyWeatherlistDTO;
	}
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public List<DailyWeatherDTO> getdaily_forecast() {
		return dailyWeatherlistDTO;
	}
	
	public void setDailyWeatherDTO(List<DailyWeatherDTO> dailyWeatherlistDTO) {
		this.dailyWeatherlistDTO = dailyWeatherlistDTO;
	}
	
	public void addDailyWeatherDTO(DailyWeatherDTO dailyWeatherDTO)
	{
		dailyWeatherlistDTO.add(dailyWeatherDTO);
	}
}
