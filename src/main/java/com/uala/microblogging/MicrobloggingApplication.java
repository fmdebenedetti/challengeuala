package com.uala.microblogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MicrobloggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrobloggingApplication.class, args);
	}

}
