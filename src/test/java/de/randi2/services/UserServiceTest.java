package de.randi2.services;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring-test.xml", "/META-INF/subconfig/security.xml"})
public class UserServiceTest {


	@Autowired private UserService userService;
	
	@Test
	public void test(){
		assertNotNull(userService);
	}
}
