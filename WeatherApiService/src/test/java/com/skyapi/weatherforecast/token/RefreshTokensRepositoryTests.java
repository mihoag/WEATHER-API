package com.skyapi.weatherforecast.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RefreshTokensRepositoryTests {
    
	@Autowired private RefreshTokenRepository repo;
	
	@Test
	public void getReFreshTokensSuccess()
	{
	  	String username = "admin";
	  	List<RefreshToken> refreshTokens = repo.findByUsername(username);
	  	assertThat(refreshTokens).isNotEmpty();
	}
	
	@Test
	public void getRefreshTokensFail()
	{
		String usernameNotFound = "abcxyz";
		List<RefreshToken> refreshTokens = repo.findByUsername(usernameNotFound);
		assertThat(refreshTokens).isEmpty();
	}
	
	@Test
	public void deleteRefreshTokenTest()
	{
	   int rows = repo.deleteByExpiryTime();
	   assertThat(rows).isGreaterThan(0);
	}
}
