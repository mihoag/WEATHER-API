package com.skyapi.weatherforecast.Location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.skyapi.weatherforecast.common.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
   
	@Query(value = "select * from location l where l.trashed = 0", nativeQuery = true)
	public List<Location> findUntrashed();
	
	@Modifying
	@Query(value = "update location set trashed = 1 where code= ?1", nativeQuery = true)
	public void trashByCode(String code);
	
	
	@Query(value = "select * from location l where l.trashed = 0 and l.code = ?1", nativeQuery = true)
	public Location get(String code);
	
	@Query(value = "select * from location l where l.trashed = 0 and l.country_code = ?1 and l.city_name = ?2" , nativeQuery = true)
	public Location findByCountryCodeAndCityName(String country, String cityName);
	
}

