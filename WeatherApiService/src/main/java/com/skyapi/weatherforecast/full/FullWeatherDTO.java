package com.skyapi.weatherforecast.full;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

import jakarta.validation.Valid;

public class FullWeatherDTO {
	
	private String location;
	
	@JsonInclude(value = JsonInclude.Include.CUSTOM,  valueFilter = RealtimeWeatherFieldFilter.class)
	@Valid
	@JsonProperty("realtime_weather")
	private RealtimeWeatherDTO realtimeWeather = new RealtimeWeatherDTO();
	
	@JsonProperty("hourly_forecast")
	@Valid
	private List<HourlyWeatherDTO> listHourlyWeather = new ArrayList<>();
	
	@JsonProperty("daily_forecast")
	@Valid
	private List<DailyWeatherDTO> listDailyWeather = new ArrayList<>();

	public FullWeatherDTO() {
		// TODO Auto-generated constructor stub
	}

	public FullWeatherDTO(String location, RealtimeWeatherDTO realTimeWeatherDTO,
		 List<HourlyWeatherDTO> listHourlyWeatherDTO, List<DailyWeatherDTO> listDailyWeatherDTO) {
		super();
		this.location = location;
		this.realtimeWeather = realTimeWeatherDTO;
		this.listHourlyWeather = listHourlyWeatherDTO;
		this.listDailyWeather = listDailyWeatherDTO;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public RealtimeWeatherDTO getRealtimeWeather() {
		return realtimeWeather;
	}

	public void setRealtimeWeather(RealtimeWeatherDTO realtimeWeather) {
		this.realtimeWeather = realtimeWeather;
	}

	public List<HourlyWeatherDTO> getListHourlyWeather() {
		return listHourlyWeather;
	}

	public void setListHourlyWeather(List<HourlyWeatherDTO> listHourlyWeather) {
		this.listHourlyWeather = listHourlyWeather;
	}

	public List<DailyWeatherDTO> getListDailyWeather() {
		return listDailyWeather;
	}

	public void setListDailyWeather(List<DailyWeatherDTO> listDailyWeather) {
		this.listDailyWeather = listDailyWeather;
	}
}
