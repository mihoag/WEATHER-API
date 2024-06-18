package com.skyapi.weatherforecast.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "Location")
public class Location {
   @Id
   private String code;
   
   @Column(length = 128, nullable = false)
   private String cityName;
   
   @Column(length = 64, nullable = false)
   private String countryName;
   
   
   @Column(length = 128)
   private String regionName;
   
   
   @Column(length = 2, nullable = false)
   private String countryCode;
   
   @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
   @PrimaryKeyJoinColumn
   private RealtimeWeather realtimeWeather; 
   
   
   @OneToMany(mappedBy = "id.location" , cascade = CascadeType.ALL, orphanRemoval = true)
   public List<HourlyWeather> listHourlyWeather = new ArrayList<>();
   
   @OneToMany(mappedBy = "id.location")
   private List<DailyWeather> listDailyWeather = new ArrayList<>();
   
   private boolean enabled;
   
   private boolean trashed;
   
  public Location() {
	// TODO Auto-generated constructor stub
  }

  public Location(String code, String cityName, String countryName, String regionName, String countryCode,
		boolean enabled, boolean trashed) {
	super();
	this.code = code;
	this.cityName = cityName;
	this.countryName = countryName;
	this.regionName = regionName;
	this.countryCode = countryCode;
	this.enabled = enabled;
	this.trashed = trashed;
  }
  
   
  
   public Location(String cityName, String countryName, String regionName, String countryCode) {
	super();
	this.cityName = cityName;
	this.countryName = countryName;
	this.regionName = regionName;
	this.countryCode = countryCode;
   }

   public String getCode() {
	 return code;
   }
   
   public void setCode(String code) {
	 this.code = code;
   }
   
   public String getCityName() {
	 return cityName;
   }
   
   public void setCityName(String cityName) {
	  this.cityName = cityName;
   }
   
   public String getCountryName() {
	  return countryName;
   }
   
   public void setCountryName(String countryName) {
	  this.countryName = countryName;
   }
   
   public String getRegionName() {
	  return regionName;
   }
   
   public void setRegionName(String regionName) {
	  this.regionName = regionName;
   }
   
   public String getCountryCode() {
	  return countryCode;
   }
   
   public void setCountryCode(String countryCode) {
	  this.countryCode = countryCode;
   }
   
   public boolean isEnabled() {
	  return enabled;
   }
   
   public void setEnabled(boolean enabled) {
	  this.enabled = enabled;
   }
   
   public boolean isTrashed() {
	 return trashed;
   }
   
   public void setTrashed(boolean trashed) {
	this.trashed = trashed;
   }



   @Override
   public int hashCode() {
	 return Objects.hash(code);
   }

   @Override
   public boolean equals(Object obj) {
	 if (this == obj)
		return true;
	 if (obj == null)
		return false;
	 if (getClass() != obj.getClass())
		return false;
	 Location other = (Location) obj;
	 return Objects.equals(code, other.code);
   }

   @Override
   public String toString() {
   return cityName + ", " + (regionName != null ? regionName + ", " : "") + countryName;
   } 


   public void copyFieldsFrom(Location another)
   {
	 setCityName(another.getCityName());
	 setRegionName(another.getRegionName());
	 setCountryCode(another.getCountryCode());
	 setCountryName(another.getCountryName());
   }

   public void copyAllFieldsFrom(Location another)
   {
	 copyFieldsFrom(another);
	 setCode(another.getCode());
	 setTrashed(another.isTrashed());
   }

   public RealtimeWeather getRealtimeWeather() {
	 return realtimeWeather;
   }

   public void setRealtimeWeather(RealtimeWeather realtimeWeather) {
	 this.realtimeWeather = realtimeWeather;
   }

   public List<HourlyWeather> getListHourlyWeather() {
	 return listHourlyWeather;
   }

   public void setListHourlyWeather(List<HourlyWeather> listHourlyWeather) {
	 this.listHourlyWeather = listHourlyWeather;
   }

   public List<DailyWeather> getListDailyWeather() {
	 return listDailyWeather;
   }

   public void setListDailyWeather(List<DailyWeather> listDailyWeather) {
	 this.listDailyWeather = listDailyWeather;
   }
}
