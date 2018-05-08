package com.upic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	// @Bean
	// public RouterFunction<?> routerFunctionA() {
	// RouterFunction<ServerResponse> route = RouterFunctions
	// .route(GET("/person/{id}").and(accept(APPLICATION_JSON)), handler::getPerson)
	// .andRoute(GET("/person").and(accept(APPLICATION_JSON)), handler::listPeople)
	// .andRoute(POST("/person"), handler::createPerson)
	// .andRoute(GET("/getJsonPerson/{id}").and(accept(APPLICATION_JSON)),
	// handler::getJsonPerson);
	// return route;
	// }
	//
	// @Bean
	// public RouterFunction<?> routerFunctionB() {
	// // ...
	// }

	// ...

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		// configure message conversion...
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// configure CORS...
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// configure view resolution for HTML rendering...
	}
}