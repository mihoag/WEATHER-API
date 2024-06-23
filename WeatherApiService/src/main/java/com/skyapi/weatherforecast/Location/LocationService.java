package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.Location;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService extends AbstractLocationService{
   
	public LocationService(LocationRepository repo)
	{
	   this.repo = repo;	
	}
   
   public Location add(Location location)
   {
	  return repo.save(location);
   }
   
   @Deprecated
   public List<Location> list()
   {
	   return repo.findUntrashed();
   }
   
   public Page<Location> listPerPage(Integer pageNum, Integer pageSize, String sortField)
   {
	   Sort sort = Sort.by(sortField).ascending();
	   Pageable pageable = PageRequest.of(pageNum-1, pageSize, sort);
	   return repo.findUntrashed(pageable);
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
   
   
}
