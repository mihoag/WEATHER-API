package com.skyapi.weatherforecast.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skyapi.weatherforecast.token.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Component
@EnableScheduling
public class RefreshTokenRemovalScheduleTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenRemovalScheduleTask.class);
	
	@Autowired private RefreshTokenRepository repo;
	
	@Scheduled(fixedDelayString  = "${app.refresh-token.removal.interval}", initialDelay = 5000)
	@Transactional
	public void deleteExpireRefreshToken()
	{
		int rows = repo.deleteByExpiryTime();
		LOGGER.info("Number of deleted refresh tokens : " + rows);
	}
	
}
