package com.upic.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.upic.po.User;
import com.upic.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * 自定义路由实现
 * @author dtz
 * @date 2018年4月26日
 * Email upicdtz@qq.com
 */
@Component
public class PersonHandler {
	@Autowired
	private UserService userService;


	public Mono<ServerResponse> listPeople(ServerRequest request) {
		// ...
		List<User> all = userService.getAll();
		Flux<User> fromIterable = Flux.fromIterable(all);
		Mono<ServerResponse> body = ServerResponse.ok().body(fromIterable,User.class);
		return body;
	}

	/**
	 * 实体类必须为反序列化类
	 * post总是会带一些奇怪的头信息
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> createPerson(ServerRequest request) {
		// ...
//		Mono<String> body = request.body(BodyExtractors.toMono(String.class));
//		Flux<User> bodyToFlux = request.bodyToFlux(User.class);
//		Mono<String> bodyToMono2 = request.bodyToMono(String.class);
		Mono<User> bodyToMono = request.bodyToMono(User.class);
		Optional<String> queryParam = request.queryParam("id");
		Optional<String> queryParam1 = request.queryParam("stuNum");
		User u=new User(Long.parseLong(queryParam.get()), "", queryParam1.get(), null);
		userService.add(u);
		return ServerResponse.ok().body(Mono.create(x->x.success(u)),User.class);
	}

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		// ...
		long attribute =Long.parseLong(request.pathVariable("id"));
		User user = userService.getUser(attribute);
		return ServerResponse.ok().body(Mono.create(x->x.success(user)),User.class);
	}
}
