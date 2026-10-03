package com.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TicketingApplicationTests {

	@Autowired
	private ApplicationContext sut;

	@Test
	void contextLoads() {
		// Arrange
		Class<TicketingApplication> applicationType = TicketingApplication.class;

		// Act
		TicketingApplication application = sut.getBean(applicationType);

		// Assert
		assertThat(application).isNotNull();
	}

}
