package com.upic.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.upic.handler.PersonHandler;
import com.upic.service.UserService;

@Configuration
public class RouteConfug {

	@Autowired
	PersonHandler handler;

	@Bean
	public RouterFunction<ServerResponse> moreMethod() {
		RouterFunction<ServerResponse> route = RouterFunctions
				.route(GET("/person/{id}").and(accept(APPLICATION_JSON)), handler::getPerson)
				.andRoute(GET("/person").and(accept(APPLICATION_JSON)), handler::listPeople)
				.andRoute(POST("/person"), handler::createPerson)
				.andRoute(GET("/getJsonPerson/{id}").and(accept(APPLICATION_JSON)), handler::getJsonPerson);

		return route;
	}
}
