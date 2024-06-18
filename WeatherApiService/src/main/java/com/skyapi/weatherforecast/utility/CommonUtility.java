package com.skyapi.weatherforecast.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtility {
	private static Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
	public static String getIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-REAL-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Handle multiple IP addresses in X-FORWARDED-FOR header
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        LOGGER.info("Client's IP Address: " + ip);

        return ip;
	}
}
