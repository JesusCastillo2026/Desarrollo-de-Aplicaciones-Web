package com.hospital.quirofano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(order = Ordered.LOWEST_PRECEDENCE - 1)
public class QuirofanoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuirofanoApiApplication.class, args);
	}

}
