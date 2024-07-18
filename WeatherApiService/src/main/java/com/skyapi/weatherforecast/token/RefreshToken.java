package com.skyapi.weatherforecast.token;

import java.util.Date;

import com.skyapi.weatherforecast.user.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, nullable = false, length = 256)
	private String token;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private user  u;
	
	private Date expiryTime;

	public RefreshToken() {
		// TODO Auto-generated constructor stub
	}

	public RefreshToken(Integer id, String token, user u, Date expiryTime) {
		super();
		this.id = id;
		this.token = token;
		this.u = u;
		this.expiryTime = expiryTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public user getU() {
		return u;
	}

	public void setU(user u) {
		this.u = u;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
}
