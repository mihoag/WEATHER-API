package com.skyapi.weatherforecast.hourly;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;

public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId>{
    @Query(value = "select * from weather_hourly wh where wh.hour_of_day >= ?2 and wh.location_code in (select l.code from location l where l.code = ?1 and l.trashed = 0)", nativeQuery =  true)
    public List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);

}
