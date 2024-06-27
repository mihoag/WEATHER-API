package com.skyapi.weatherforecast.location;

import java.util.List;
import java.util.Map;

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
   
   
   @Deprecated
   public Page<Location> listPerPage(Integer pageNum, Integer pageSize, String sortField)
   {
	   Sort sort = Sort.by(sortField).ascending();
	   Pageable pageable = PageRequest.of(pageNum-1, pageSize, sort);
	   return repo.findUntrashed(pageable);
   }
   
   public Page<Location> listByPage(int pageNum, int pageSize, String sortOption, Map<String, Object> filterFields) {
		Sort sort = createMultipleSorts(sortOption);
		
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
		
		return repo.listWithFilter(pageable, filterFields);
	}
   
   private Sort createMultipleSorts(String sortOption) {
		String[] sortFields = sortOption.split(",");
		
		Sort sort = null;
		
		if (sortFields.length > 1) { // sorted by multiple fields
			
			sort = createSingleSort(sortFields[0]);
			
			for (int i = 1; i < sortFields.length; i++) {
				
				sort = sort.and(createSingleSort(sortFields[i]));
			}
			
		} else { // sorted by a single field
			sort = createSingleSort(sortOption);
		}
		return sort;
	}	
	
	private Sort createSingleSort(String fieldName) {
		String actualFieldName = fieldName.replace("-", "");
		return fieldName.startsWith("-")
					? Sort.by(actualFieldName).descending() : Sort.by(actualFieldName).ascending();		
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
