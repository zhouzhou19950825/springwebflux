package com.upic.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

	/**
	 * @RequestParam
	 * @param id
	 * @param username
	 * @return
	 */
	@GetMapping("/getUpic")
	public String setupForm(String id, @RequestParam("user") String username) {
		return id + username;
	}

	/**
	 * http://localhost:8088/getUpics?id=5&user=dongshuai
	 * 
	 * @param maps
	 * @return MultiValueMap<String, String> Map<String,String>maps
	 * 
	 *         以上两种类型被@RequestParam修饰，所有的参数会以键值对被注入
	 */
	@GetMapping("/getUpics")
	public Map<String, String> setupForm(@RequestParam Map<String, String> maps) {
		return maps;
	}

	/**
	 * 获取头信息
	 * 
	 * @param encoding
	 * @param keepAlive
	 */
	@GetMapping("/demoHandle")
	public String handle(@RequestHeader("Accept-Encoding") String encoding,
			@RequestHeader("Keep-Alive") long keepAlive) {
		return encoding + keepAlive;
	}

	/**
	 * http://localhost:8088/demoHandles 当@RequestHeader注解上的使用Map<String, String>，
	 * MultiValueMap<String, String>或HttpHeaders参数，则map被填充有所有标头值
	 * 
	 * @param maps
	 * @return
	 */
	@GetMapping("/demoHandles")
	public Map<String, String> handleList(@RequestHeader Map<String, String> maps) {
		return maps;
	}

	/**
	 * 带注释的方法参数@RequestHeader("Accept")可以是类型的 String，但也String[]还是List<String>。
	 * 
	 * @param accept
	 * @return
	 */
	@GetMapping("/demoAccept")
	public String handleAccept(@RequestHeader("Accept") String accept) {
		return accept;
	}

	@GetMapping("/demoAcceptArray")
	public String[] handleAcceptArray(@RequestHeader("Accept") String[] accept) {
		return accept;
	}

	@GetMapping("/demoAcceptList")
	public List<String> handleAcceptList(@RequestHeader("Accept") List<String> accept) {
		return accept;
	}
}
