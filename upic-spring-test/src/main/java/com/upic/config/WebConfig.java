package com.upic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.upic.config.format.DateFormatter;

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
	/**
	 * 定好时间格式
	 * 如果有全局InitBinder的话，将不起效果
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
	}

	/**
	 * 跨域设置
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**").allowedOrigins("http://domain2.com").allowedMethods("PUT", "DELETE")
				.allowedHeaders("header1", "header2", "header3").exposedHeaders("header1", "header2")
				.allowCredentials(true).maxAge(3600);
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	}
}