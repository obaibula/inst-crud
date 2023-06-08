package com.example.instcrud;

import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
import com.example.instcrud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class InstCrudApplicationTests {

	//Don't forget to change your timezone from 'Europe/Kyiv' to something else, such as Helsinki.
	@Container
	public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.8")
			.withUsername("postgres")
			.withPassword("12345")
			.withDatabaseName("myinstagram");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.username", container::getUsername);
	}
	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads(){
		var user = User.builder()
				.username("user")
				.bio("fdfh")
				.avatar("userUpdate.getAvatar()")
				.phone("userUpdate.getPhone()")
				.email("userUpdate.getEmail()")
				.password(null)
				.status(UserStatus.ONLINE)
				.build();

		userRepository.save(user);

		System.out.println("Context loads!");
	}

}
