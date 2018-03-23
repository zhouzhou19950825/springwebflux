package com.upic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
