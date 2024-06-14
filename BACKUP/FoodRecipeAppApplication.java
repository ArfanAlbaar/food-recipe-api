package com.uas.kelompoksatu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class FoodRecipeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodRecipeAppApplication.class, args);
	}

}
