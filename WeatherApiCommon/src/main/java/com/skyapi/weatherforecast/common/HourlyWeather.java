package com.skyapi.weatherforecast.common;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_hourly")
public class HourlyWeather {

	@EmbeddedId
	private HourlyWeatherId id = new HourlyWeatherId();
	
	private int temperature;
	private int precipitation;
	
	@Column(length = 50)
	private String status;

	public HourlyWeatherId getId() {
		return id;
	}

	public void setId(HourlyWeatherId id) {
		this.id = id;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HourlyWeather other = (HourlyWeather) obj;
		return Objects.equals(id, other.id);
	}
	
	public HourlyWeather getShallowCopy() {
		HourlyWeather copy = new HourlyWeather();
		copy.setId(this.getId());
		return copy;
	}
	
	@Override
	public String toString() {
		return "HourlyWeather [id=" + id + ", temperature=" + temperature + ", precipitation=" + precipitation
				+ ", status=" + status + "]";
	}
}
