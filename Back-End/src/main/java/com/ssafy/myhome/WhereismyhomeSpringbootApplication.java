package com.ssafy.myhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.ssafy.myhome.**"})
public class WhereismyhomeSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereismyhomeSpringbootApplication.class, args);
	}
}
