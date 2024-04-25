package com.skyapi.weatherforecast.Location;

public class LocationNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LocationNotFoundException(String code)
	{
		super("No location found with the given code: " + code);	
	}
	
	public LocationNotFoundException(String countryCode, String  cityName)
	{
		super("No location found with the given country code: " + countryCode + " and city name: " + cityName);	
	}
}
