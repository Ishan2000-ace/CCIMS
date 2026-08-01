package com.Forensics.CCIMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CcimsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcimsApplication.class, args);
	}

}
