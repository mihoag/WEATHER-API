package com.skyapi.weatherforecast.Location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService {
   @Autowired
   private LocationRepository repo;
   
   public Location add(Location location)
   {
	  return repo.save(location);
   }
   
   public List<Location> list()
   {
	   return repo.findUntrashed();
   }
   public Location update(Location locationInRequest)
   {
	   String code = locationInRequest.getCode();
	   Location locationDb  = repo.findById(code).get();
	   if(locationDb == null)
	   {
		   throw new LocationNotFoundException(code);
	   }
	   locationDb.copyFieldsFrom(locationInRequest);
	   return repo.save(locationDb);
   }
   
   public void delete(String code)  
   {
	   try {
		   System.out.println(code);
		   repo.trashByCode(code);
	  } catch (Exception e) {
		// TODO: handle exception
		  throw new LocationNotFoundException(code);
	  }
	 
   }
   
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
