package com.skyapi.weatherforecast.realtime;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skyapi.weatherforecast.common.RealtimeWeather;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {
   @Query(value = "select * from realtime_weather r where r.location_code in (select l.code from location l where l.country_code= ?1 and l.city_name = ?2)", nativeQuery = true)
   public RealtimeWeather findByCountryCodeAndCity(String countryCode, String cityname);
   
   @Query(value = "select * from  realtime_weather r where  r.location_code in (select l.code from location l where l.code = ?1 and l.trashed = 0) ", nativeQuery = true)
   public RealtimeWeather findByLocationCode(String locationCode);
}
