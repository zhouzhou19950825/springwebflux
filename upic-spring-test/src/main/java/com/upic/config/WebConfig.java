package com.upic.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.upic.config.format.ClassValidator;
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
	 * 要自定义请求的内容类型解析 主要增加媒体类型MediaType
	 */
	@Override
	public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {

	}

	/**
	 * 定好时间格式 如果有全局InitBinder的话，将不起效果
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
	}

	/**
	 * HTTP消息编解码器
	 */
	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
	}

	/**
	 * 验证类
	 */
	@Override
	public Validator getValidator() {
		return new ClassValidator();
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

	/**
	 * 文档configurePathMatch错误，应修改configurePathMatching
	 * setUseCaseSensitiveMatch 是否区分大小写 默认false (为false的时候不管大小写)
	 * setUseTrailingSlashMatch 是否区分尾斜线 默认true
	 */
	@Override
	public void configurePathMatching(PathMatchConfigurer configurer) {
		// ...
		configurer.setUseCaseSensitiveMatch(false);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/public", "classpath:/static/")
				.setCacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS));
		// 这个方法不存在，spring webflux文档中的bug
		// .setCachePeriod(31556926);
	}
}