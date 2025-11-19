package com.ihy.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IhySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IhySystemApplication.class, args);
	}

}
