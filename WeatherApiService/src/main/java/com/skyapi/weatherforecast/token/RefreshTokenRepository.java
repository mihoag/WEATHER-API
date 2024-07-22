package com.skyapi.weatherforecast.token;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    
	@Query("select rt from RefreshToken rt where rt.u.username = ?1")
	public List<RefreshToken> findByUsername(String username);
	
	@Query("delete from RefreshToken rt where rt.expiryTime <= CURRENT_TIME")
	@Modifying
	public int deleteByExpiryTime();
}
