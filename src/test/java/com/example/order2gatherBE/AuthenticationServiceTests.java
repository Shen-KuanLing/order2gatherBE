package com.example.order2gatherBE;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Assertions;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.example.order2gatherBE.services.AuthenticationService;

@SpringBootTest
public class AuthenticationServiceTests {
	@Autowired
	private AuthenticationService authenticationService;

	@MockBean
	private AuthenticationRepository authenticationRepository;

	@Test
	void login() {
		UserModel user1 = new UserModel(); // user1 exist in db
		user1.setId(1);
		user1.setGmail("test1@gmail.com");
		user1.setUsername("user1");
		user1.setLastLogin(new Timestamp(new Date().getTime()));

		Mockito.when(authenticationRepository.findUserbyGmail(user1.getGmail())).thenReturn(user1);

		String token = authenticationService.login(user1.getGmail(), user1.getUsername());

		// Test the jwt token is valid
		Assertions.assertEquals(authenticationService.verify(token), user1.getId());

		String invaildToken = token + "token";
		// The token should be invalid if token have been modified
		Assertions.assertEquals(authenticationService.verify(invaildToken), -1);

		// TODO: Test expired token

		// TODO: Test the unregistered user
	}
}
