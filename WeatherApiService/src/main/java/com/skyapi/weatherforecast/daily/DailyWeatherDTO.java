package com.skyapi.weatherforecast.daily;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO  {
	
	@Range(min = 1 , max = 31, message = "Day must be in the range of 1 to 31")
	@JsonProperty("day_of_month")
	private int dayOfMonth;
	
	@Range(min = 1 , max = 12, message = "Month must be in the range of 1 to 12")
	private int month;
	
	@JsonProperty("min_temp")
	private int minTemp;
  
	@JsonProperty("max_temp")
	private int maxTemp;
	
	@Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
	private int precipitation;
	
	@NotBlank(message = "Status must not be empty")
	@Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")	
	private String status;

	public DailyWeatherDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public DailyWeatherDTO dayOfMonth(int dayOfMonth)
	{
		this.dayOfMonth = dayOfMonth;
		return this;
	}
	
	public DailyWeatherDTO month(int month)
	{
		this.month = month;
		return this;
	}
	
	public DailyWeatherDTO minTemp(int minTemp)
	{
		this.minTemp = minTemp;
		return this;
	}
	
	public DailyWeatherDTO maxTemp(int maxTemp)
	{
		this.maxTemp = maxTemp;
		return this;
	}
	
	public DailyWeatherDTO precipitation(int precipitation)
	{
		this.precipitation = precipitation;
		return this;
	}
	
	public DailyWeatherDTO status(String status)
	{
		this.status = status;
		return this;
	}
	
	public DailyWeatherDTO(int dayOfMonth,
			 int month, int minTemp,
			int maxTemp,
			 int precipitation,
			String status) {
		super();
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.precipitation = precipitation;
		this.status = status;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
	}

	public int getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
		this.maxTemp = maxTemp;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
