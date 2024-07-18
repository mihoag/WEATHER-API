package com.skyapi.weatherforecast.user;

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
public class RoleRepositoryTest {
	@Autowired
    private RoleRepository repo;
    
    @Test
    public void addRoleSuccess()
    {
    	role role1 = new role().name("READ");
    	role role2 = new role().name("ADD");
    	role role3 = new role().name("UPDATE");
    	role role4 = new role().name("DELETE");
    	
    	List<role> lsRoles = repo.saveAll(List.of(role1, role2, role3, role4));
    	
    	assertThat(lsRoles).isNotEmpty();
    	assertThat(lsRoles.size()).isEqualTo(4);
    }
    
    @Test
    public void getRoleSuccess()
    {
    	role role1 = repo.getRolesByName("READ"); 
		role role2 = repo.getRolesByName("ADD");
		role role3 = repo.getRolesByName("UPDATE");
		role role4 = repo.getRolesByName("DELETE");
		
	    assertThat(role1.getName()).isEqualTo("READ");
	    assertThat(role2.getName()).isEqualTo("ADD");
	    assertThat(role3.getName()).isEqualTo("UPDATE");
	    assertThat(role4.getName()).isEqualTo("DELETE");
    }
}
