package com.skyapi.weatherforecast.PasswordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
		String password = "123456";
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		String encodedPassword = bpe.encode(password);
		System.out.println(encodedPassword);
	}
}
