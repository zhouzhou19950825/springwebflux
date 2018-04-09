package com.upic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
//@EnableWebFluxSecurity
//@EnableWebFlux
public class UpicSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpicSpringTestApplication.class, args);
	}
	
//	@Bean
//	public RequestMappingHandlerMapping register() {
//		return new MyRequestMappingHandler();
//	}
}
