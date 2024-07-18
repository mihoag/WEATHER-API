package com.skyapi.weatherforecast.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<role, Integer> {
	
   @Query(name = "select * from roles where name = ?1", nativeQuery = true)
   public role getRolesByName(String name);
}
