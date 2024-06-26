package com.skyapi.weatherforecast.common_service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.exception_handler.GeolocationException;

@Service
public class GeolocationService {
   private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);
   private String DBPath = "/ip2locdb/IP2LOCATION-LITE-DB3.BIN";
   private IP2Location ipLocator = new IP2Location();
   
   
   public GeolocationService()
   {
	   try {
		InputStream inputStream = getClass().getResourceAsStream(DBPath);
		byte[] data = inputStream.readAllBytes();
		ipLocator.Open(data);
		inputStream.close();
	} catch (Exception e) {
		// TODO: handle exception
		LOGGER.error(e.getMessage(), e);
	}
   }
   
   public Location getLocation(String ipAddress) throws GeolocationException {
		
		try {
			IPResult result = ipLocator.IPQuery(ipAddress);
			
			if (!"OK".equals(result.getStatus())) {
				throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
			}
			
			LOGGER.info(result.toString());
			
			return new Location(result.getCity(), result.getCountryLong(), result.getRegion(), result.getCountryShort());
		} catch (IOException ex) {
			throw new GeolocationException("Error querying IP database", ex);
		}
		
   }
   
   public static void main(String[] args) {
	   GeolocationService ser = new GeolocationService();
	   System.out.println(ser.getLocation("102.176.175.255"));
}
}
