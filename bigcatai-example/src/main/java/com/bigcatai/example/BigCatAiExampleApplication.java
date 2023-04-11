package com.bigcatai.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bigcatai"})   
public class BigCatAiExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigCatAiExampleApplication.class, args);
	}

}
