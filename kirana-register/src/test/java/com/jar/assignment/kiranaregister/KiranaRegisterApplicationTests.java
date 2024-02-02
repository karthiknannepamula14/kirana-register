package com.jar.assignment.kiranaregister;

import com.jar.assignment.kiranaregister.controller.TransactionController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class KiranaRegisterApplicationTests {

	@Autowired
	private TransactionController transactionController;

	@Test
	void contextLoads() {
		// Simple test to check if the Spring context loads successfully
		assertNotNull(transactionController);
	}

}
