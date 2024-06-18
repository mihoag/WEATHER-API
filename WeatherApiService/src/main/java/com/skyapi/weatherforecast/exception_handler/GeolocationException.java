package com.skyapi.weatherforecast.exception_handler;

public class GeolocationException extends RuntimeException {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -7318185221464457510L;

	public GeolocationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public GeolocationException(String message) {
		super(message);
	}
}
