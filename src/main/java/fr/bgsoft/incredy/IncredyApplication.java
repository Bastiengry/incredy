package fr.bgsoft.incredy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class IncredyApplication {

	public static void main(final String[] args) {
		initializeApplication(args);
	}

	static ConfigurableApplicationContext initializeApplication(final String[] args) {
		return SpringApplication.run(IncredyApplication.class, args);
	}
}
