package com.skyapi.weatherforecast.Config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.RealtimeWeather;
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
}
