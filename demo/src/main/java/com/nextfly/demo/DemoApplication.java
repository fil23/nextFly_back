package com.nextfly.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		// System.setProperty("DB_URL", dotenv.get("DB_URL"));
		// System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		// System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		// System.setProperty("TOKEN_SECRET", dotenv.get("TOKEN_SECRET"));
		// System.setProperty("MAIL", dotenv.get("MAIL"));
		// System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		SpringApplication.run(DemoApplication.class, args);
	}

}
