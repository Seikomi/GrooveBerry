package com.seikomi.grooveberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.seikomi"})
@EnableJpaAuditing
public class GrooveberryWebappApplication {

	public static void main(String[] args) {		
		SpringApplication.run(GrooveberryWebappApplication.class, args);
	}
}
