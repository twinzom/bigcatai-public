package com.bigcatai.matchingengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.wmgpt", "com.bigcatai"})   
public class BigCatAiMatchingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigCatAiMatchingEngineApplication.class, args);
	}

}
