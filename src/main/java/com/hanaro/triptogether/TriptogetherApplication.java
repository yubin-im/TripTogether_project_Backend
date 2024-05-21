package com.hanaro.triptogether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TriptogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriptogetherApplication.class, args);
	}

}
