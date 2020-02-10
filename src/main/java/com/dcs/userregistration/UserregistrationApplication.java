package com.dcs.userregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class UserregistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserregistrationApplication.class, args);
	}

}
