package com.upic.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * allowedCredentials
 * 默认情况下不启用，因为它建立了一个信任级别，暴露敏感的用户特定信息，如cookie和CSRF令牌，并且只能在适当的时候使用。
 * 
 * maxAge 设置为30分钟。
 * 
 * @author dongtengzhou
 *
 */
//@CrossOrigin(maxAge = 3600)
@RestController
public class CorsController {

//	@CrossOrigin(value="http://192.168.7.29")
	@CrossOrigin
	@GetMapping("cors/{id}")
	public Mono<Long> retrieve(@PathVariable Long id) {
		// ...
		return Mono.create(x -> {
			x.success(id);
		});
	}
}
