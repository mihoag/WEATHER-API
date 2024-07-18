package com.skyapi.weatherforecast.security.jwt;

public class JwtValidationException  extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtValidationException(String message, Throwable throwable)
    {
       super(message, throwable);
    }
}
