package com.skyapi.weatherforecast;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

public abstract class AbstractLocationService {
	protected LocationRepository repo;
	
	public Location getByCode(String code)
	   {
		   Location location = repo.get(code);
		   if(location == null)
		   {
			   throw new LocationNotFoundException(code);
		   }
		   return location;
	   }
}
