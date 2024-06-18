package com.skyapi.weatherforecast.full;

import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

public class RealtimeWeatherFieldFilter {

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
	    if(obj instanceof RealtimeWeatherDTO)
	    {
	        RealtimeWeatherDTO dto = (RealtimeWeatherDTO) obj;
	        if(dto.getStatus() == null)
	        {
	        	return true;
	        }
	    }
		return false;
	}
}
