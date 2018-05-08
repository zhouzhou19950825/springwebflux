package com.upic.config;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.upic.handler.PersonHandler;

@Configuration
public class RouteConfug {

	@Autowired
	PersonHandler handler;

	private List<String> allowUrl=new ArrayList<String>();
	
	public RouteConfug() {
		allowUrl.add("/person");
	}
	/**
	 * RequestPredicate.and(RequestPredicate) - 两者都必须匹配。
	 * 
	 * RequestPredicate.or(RequestPredicate) - 可能匹配。
	 * 
	 * RouterFunction.and(RouterFunction)
	 * 
	 * RouterFunction.andRoute(RequestPredicate, HandlerFunction) -
	 * RouterFunction.and()嵌套的快捷方式 RouterFunctions.route()。
	 * 
	 * @return
	 */
	@Bean
	public RouterFunction<ServerResponse> moreMethod() {
		RouterFunction<ServerResponse> route = RouterFunctions
				.route(GET("/person/{id}").and(accept(APPLICATION_JSON)), handler::getPerson)
				.andRoute(GET("/person").and(accept(APPLICATION_JSON)), handler::listPeople)
				.andRoute(POST("/person"), handler::createPerson)
				.andRoute(GET("/getJsonPerson/{id}").and(accept(APPLICATION_JSON)), handler::getJsonPerson);
		// 加一个filter
		// route.filter((request, next) -> {
		// if (securityManager.allowAccessTo(request.path())) {
		// return next.handle(request);
		// } else {
		// return ServerResponse.status(UNAUTHORIZED).build();
		// }
		// });
		
		//增加了过滤器
		RouterFunction<ServerResponse> filter = route.filter((request, next) -> {
			for(String url :allowUrl) {
				if(request.path().startsWith(url)) {
					return next.handle(request);
				}
			}
			return ServerResponse.status(UNAUTHORIZED).build();
		});
		return filter;
	}

}
