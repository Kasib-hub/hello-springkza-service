package com.kizza.helloworld;

import com.kizza.helloworld.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HelloworldApplicationTests {
	private static final String BASE_ENDPOINT = "/api/v1/students";

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@DisplayName("Success GET /students")
	// You have to use exchange() to describe the List we want to create
	void testGetStudents() {
		final ResponseEntity<List<Student>> response = restTemplate.exchange(
				BASE_ENDPOINT,
				HttpMethod.GET,
				null,
                new ParameterizedTypeReference<>() {}
		);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody().size()).isEqualTo(3);
	}

}
