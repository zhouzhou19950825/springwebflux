package com.upic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

import com.upic.po.User;

@Controller
public class RedirectController {

	/**
	 * 重定向，通过返回字符串
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "redirect:/page/index.html";
	}

	/**
	 * 绝对路径URL
	 * 
	 * @return
	 */
	@RequestMapping("/loginhttp")
	public String redirectLogin() {
		return "redirect:http://localhost:8081/page/index.html";
	}

	/**
	 * @ModelAttribute 也可以用作方法的方法级注释，@RequestMapping
	 *                 在这种情况下方法的返回值@RequestMapping被解释为模型属性。这通常不是必需的，
	 *                 因为它是HTML控制器中的默认行为，除非返回值是否String会被视为视图名称。 @ModelAttribute也可以帮助定制模型属性名称：
	 */
//	@GetMapping("/page/{id}")
////	@ModelAttribute("/page/index.html")
//	public User handleUser(@PathVariable String id) {
//		// ...
//		return new User(1L, "1422110108", "c", "d");
//	}
}
