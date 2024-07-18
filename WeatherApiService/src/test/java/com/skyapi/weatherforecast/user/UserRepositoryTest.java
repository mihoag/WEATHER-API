package com.skyapi.weatherforecast.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.management.relation.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired private UserRepository userRepo;
	
	@Autowired private RoleRepository roleRepo;

	
	@Test
	public void addUserSuccess()
	{
		role role1 = roleRepo.getRolesByName("READ"); 
		role role2 = roleRepo.getRolesByName("ADD");
		role role3 = roleRepo.getRolesByName("UPDATE");
		role role4 = roleRepo.getRolesByName("DELETE");
		
		user user1 = new user();
		user1.setUsername("admin");
		user1.setPassword("$2a$10$CMAXMaLL8i0/DCIR4AirUuP1i4C4.bCY2gvaJaG4qqlCFGc.pvs/2");
	    user1.addRoles(role1);
	    user1.addRoles(role2);
	    user1.addRoles(role3);
	    user1.addRoles(role4);
	    
	    
	    user user2 = new user();
	    user2.setUsername("minhhoang");
	    user2.setPassword("$2a$10$CMAXMaLL8i0/DCIR4AirUuP1i4C4.bCY2gvaJaG4qqlCFGc.pvs/2");
	    user2.addRoles(role1);
	    
	    
		List<user> lsUsers = userRepo.saveAll(List.of(user1, user2));
		
		assertThat(lsUsers).isNotEmpty();
		assertThat(lsUsers.size()).isEqualTo(2);
	}
	
	@Test
	public void getUserByUsernameSuccess()
	{
	    String username = "admin";
	    user u = userRepo.getUserByUsername(username);
	    assertThat(u).isNotNull();
	    assertThat(u.getUsername()).isEqualTo(username);
	}

	@Test
	public void getUserByUsernameFail()
	{
	    String username = "abcxyz";
	    user u = userRepo.getUserByUsername(username);
	    assertThat(u).isNull();
	}
}
