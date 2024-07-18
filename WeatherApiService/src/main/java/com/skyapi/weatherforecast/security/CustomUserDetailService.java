package com.skyapi.weatherforecast.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.user.UserRepository;
import com.skyapi.weatherforecast.user.user;


@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		user u = repo.getUserByUsername(username);
		if(u == null)
		{
			throw new UsernameNotFoundException("User not found!");
		}
		return new CustomUserDetails(u);
	}

}
