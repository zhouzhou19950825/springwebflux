package com.upic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
	
	@GetMapping("/hello")
    public String handle() {
        return "Hello WebFlux";
    }
	@GetMapping("/{id}")
	public String returnChar(@PathVariable String id) {
		return id;
	}
	/**
	 * Spring WebFlux不支持后缀模式匹配 - 与Spring MVC不同，后者的映射/person也与之匹配/person.*
	 * 
	 * 但是测试之后发现也是可行的，这个是个官方文档可能出现的一个问题
	 */
	@GetMapping("/person.*")
	public String getPerson() {
		return "a";
	}
	@GetMapping("/getA")
	public String getA(String a) {
		return a;
	}
}
