package com.skyapi.weatherforecast.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
public class role {
	
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
    
    @NotNull(message = "Role name is not null")
    private String nane;


	public role() {
		// TODO Auto-generated constructor stub
	}
	public role(Integer id, @NotNull(message = "Role name is not null") String nane) {
		super();
		this.id = id;
		this.nane = nane;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNane() {
		return nane;
	}
	public void setNane(String nane) {
		this.nane = nane;
	}
	
	public role name(String name)
	{
		setNane(name);
		return this;
	}
   
}
