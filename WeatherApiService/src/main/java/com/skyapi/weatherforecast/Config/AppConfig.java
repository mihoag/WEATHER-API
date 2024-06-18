package com.skyapi.weatherforecast.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

@Configuration
public class AppConfig {
	
	@Bean
    public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		configureMappingForHourlyWeather(mapper);	
		configureMappingForRealtimeWeather(mapper);
		configureMappingForDailyWeather(mapper);
		configureMappingForFullWeather(mapper);
		return mapper;
    }
	
	private void configureMappingForRealtimeWeather(ModelMapper mapper) {
		mapper.typeMap(RealtimeWeatherDTO.class, RealtimeWeather.class)
			.addMappings(m -> m.skip(RealtimeWeather::setLocation));
	}
	
	private void configureMappingForHourlyWeather(ModelMapper mapper) {
		mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class)
			.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);
		
		mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class)
			.addMapping(src -> src.getHourOfDay(), 
				(dest, value) ->	dest.getId().setHourOfDay(value != null ? (int) value : 0));
	}
	
	private void configureMappingForFullWeather(ModelMapper mapper) {
		mapper.typeMap(Location.class, FullWeatherDTO.class)
			.addMapping(src -> src.toString(), FullWeatherDTO::setLocation);
	}
	
	private void configureMappingForDailyWeather(ModelMapper mapper)
	{
		mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)
		.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth)
		.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
		
		mapper.typeMap( DailyWeatherDTO.class, DailyWeather.class)
		.addMapping(src -> src.getDayOfMonth(), (des, value)-> des.getId().setDayOfMonth(value != null ? (int) value : 0))
        .addMapping(src -> src.getMonth(), (des, value) -> des.getId().setMonth(value != null ? (int) value : 0));
	}
	
	
}
