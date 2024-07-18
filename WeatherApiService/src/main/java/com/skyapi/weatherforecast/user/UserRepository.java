package com.skyapi.weatherforecast.user;

import org.aspectj.weaver.tools.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<user, Integer> {
	@Query(name =  "select * from user where username = ?1", nativeQuery = true)
    public user getUserByUsername(String username);
}
