package com.upic.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upic.po.User;

import reactor.core.publisher.Mono;

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

	@GetMapping("/getcookie")
	public String handle(@CookieValue("JSESSIONID") String cookie) {
		return cookie;
	}

	/**
	 * @ModelAttribute 对象属性匹配 如果对象未标注（不是基本数据类型），默认为标注
	 * @param user
	 * @return
	 */
	@GetMapping("/user/{id}/users/{stuName}/edit")
	// public User processSubmit(@ModelAttribute User user) {
	public User processSubmit(User user) {
		return user;
	}

	/**
	 * 字段匹配异常错误会注入到BindingResult result对象
	 * 
	 * @param pet
	 * @param result
	 * @return
	 */
	@GetMapping("/owners/{id}/pets/{stuName}/edit/error")
	public String processSubmit(@ModelAttribute User pet, BindingResult result) {
		if (result.hasErrors()) {
			return result.getAllErrors().toString();
		}
		return pet.toString();
	}

	/**
	 * url:http://localhost:8088/user/1/pets/a/edit?id=aa&stuName=dtz //error
	 * Spring WebFlux支持模型中的反应类型，例如 Mono<Account>或io.reactivex.Single<Account>
	 * @param petMono
	 * @return
	 */
	@GetMapping("/user/{id}/pets/{stuName}/edit")
	public Mono<String> processSubmit( @ModelAttribute("user") Mono<User> petMono) {
		return petMono.map(x->x.toString()).onErrorResume(ex -> {
			return Mono.create(x -> x.success(ex.getMessage()));
		});
	}
	
	/**
	 * url:http://localhost:8088/user/edit?id=aa&stuName=dtz
	 * 测试BindingResult在Mono修饰的时候是否有用
	 * @param petMono
	 * @param result
	 * @return
	 */
	//error 无法注入Failed to resolve argument 1 of type 'org.springframework.validation.BindingResult
	@GetMapping("/user/edit")
	public Mono<String> processSubmit01( @ModelAttribute("user") Mono<User> petMono, BindingResult result) {
		if (result.hasErrors()) {
			return Mono.create(x->x.success(result.getAllErrors().toString()));
		}
		return petMono.map(x->x.toString()).onErrorResume(ex -> {
			return Mono.create(x -> x.success(ex.getMessage()));
		});
	}
}
