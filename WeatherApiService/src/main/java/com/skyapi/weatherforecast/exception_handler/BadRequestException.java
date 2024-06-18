package com.skyapi.weatherforecast.exception_handler;

public class BadRequestException extends Exception {
	public BadRequestException(String message) {
		super(message);
	}
}
