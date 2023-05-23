package com.example.instcrud;

import com.example.instcrud.entity.UserStatus;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
// todo: refactor tests using postgres db deployed in docker
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstCrudApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnAUserWhenDataIsSaved(){
		ResponseEntity<String> response = restTemplate
				.getForEntity("/users/1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		var documentContext = JsonPath.parse(response.getBody());
		var id = documentContext.read("$.id");
		assertThat(id).isEqualTo(1);

		var username = documentContext.read("$.username");
		assertThat(username).isEqualTo("oleh123");

		var avatar = documentContext.read("$.avatar");
		assertThat(avatar).isNull();

		var status = documentContext.read("$.status");
		assertThat(status).isEqualTo(UserStatus.OFFLINE.toString());
	}

}
