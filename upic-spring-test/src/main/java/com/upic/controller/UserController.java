package com.upic.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upic.po.User;
import com.upic.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/addUser")
	public Mono<User> addUser(User user) {
		return Mono.create(x -> x.success(userService.add(user)));
	}
	@GetMapping("/getAll")
	public Flux<User> getAll() {
		return Flux.create(x -> {
			userService.getAll().forEach(user -> x.next(user));
			x.complete();
		});
	}
	@GetMapping("/getDate")
	public Date getDate(Date date) {
		return date;
	}
}
