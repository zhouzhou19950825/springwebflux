package com.upic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {
	/**
	 * 跨域全局配置 所有的起源。
	 * 
	 * 所有标题。
	 * 
	 * GET，HEAD和POST方法。
	 * 
	 * allowedCredentials
	 * 默认情况下不启用，因为它建立了一个信任级别，暴露敏感的用户特定信息，如cookie和CSRF令牌，并且只能在适当的时候使用。
	 * 
	 * maxAge 设置为30分钟。
	 * 
	 * 
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/api/**").allowedOrigins("http://domain2.com").allowedMethods("PUT", "DELETE")
				.allowedHeaders("header1", "header2", "header3").exposedHeaders("header1", "header2")
				.allowCredentials(true).maxAge(3600);
	}
}
