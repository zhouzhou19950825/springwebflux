package com.upic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import com.upic.mappinghandler.MyRequestMappingHandler;

@SpringBootApplication
public class UpicSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpicSpringTestApplication.class, args);
	}
	
	@Bean
	public RequestMappingHandlerMapping register() {
		return new MyRequestMappingHandler();
	}
}
