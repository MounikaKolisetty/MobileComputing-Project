package com.rescuer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescuer.api.web.dto.CreateTicketDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
public class RescuerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RescuerApplication.class, args);
	}

	@Component
	public static class StringToUserConverter implements Converter<String, CreateTicketDTO> {

		@Autowired
		private ObjectMapper objectMapper;

		@Override
		@SneakyThrows
		public CreateTicketDTO convert(String source) {
			return objectMapper.readValue(source, CreateTicketDTO.class);
		}

	}

	/*@Bean
	ApplicationRunner init(UserRepository repository, BCryptPasswordEncoder encoder) {
		return args -> {
			repository.save(User.builder().isActive(true).userName("testuser1").userType(UserType.ADMIN).password(encoder.encode("sample").toCharArray()).build());
			repository.findAll().forEach(System.out::println);
		};
	}*/
}
