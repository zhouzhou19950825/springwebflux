package com.upic.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.upic.po.User;
import com.upic.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 自定义路由实现 ServerResponse
 * 
 * @author dtz
 * @date 2018年4月26日 Email upicdtz@qq.com
 */
@Component
public class PersonHandler {
	@Autowired
	private UserService userService;

	public Mono<ServerResponse> listPeople(ServerRequest request) {
		// ...
		List<User> all = userService.getAll();
		Flux<User> fromIterable = Flux.fromIterable(all);
		Mono<ServerResponse> body = ServerResponse.ok().body(fromIterable, User.class);
		return body;
	}

	/**
	 * 实体类必须为反序列化类 post总是会带一些奇怪的头信息
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> createPerson(ServerRequest request) {
		// ...
		// ServerRequest.body(BodyExtractor) 它接受BodyExtractor功能性策略界面
		// Mono<String> body = request.body(BodyExtractors.toMono(String.class));
		// 要提取的身体的Flux<Person>，其中Person的对象是从一些序列化形式，诸如JSON或XML解码
		// Flux<User> bodyToFlux = request.bodyToFlux(User.class);
		// Mono<String> bodyToMono2 = request.bodyToMono(String.class);
		// 要访问表单数据
		Mono<MultiValueMap<String, String>> map = request.body(BodyExtractors.toFormData());
		MultiValueMap<String, String> block = map.block();
		Map<String, String> singleValueMap = block.toSingleValueMap();
		String list = singleValueMap.get("id");
		String list1 = singleValueMap.get("stuNum");

		// Optional<String> queryParam = request.queryParam("id");
		//
		// Optional<String> queryParam1 = request.queryParam("stuNum");
		User u = new User(Long.parseLong(list), "", list1, null);
		userService.add(u);
		return ServerResponse.ok().body(Mono.create(x -> x.success(u)), User.class);
	}

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		// ...
		long attribute = Long.parseLong(request.pathVariable("id"));
		User user = userService.getUser(attribute);
		/**
		 * 通过lambda构建
		 */
		HandlerFunction<ServerResponse> helloWorld = r -> ServerResponse.ok().body(Mono.create(x -> x.success(user)),
				User.class);
		return helloWorld.handle(request);
	}

	public Mono<ServerResponse> getJsonPerson(ServerRequest request) {
		long attribute = Long.parseLong(request.pathVariable("id"));
		User user = userService.getUser(attribute);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.create(x -> x.success(user)),
				User.class).switchIfEmpty(ServerResponse.notFound().build());
	}
}
