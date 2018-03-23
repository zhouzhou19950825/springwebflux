package com.upic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

@Controller
public class RedirectController {

	/**
	 * 重定向，通过返回字符串
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "redirect:/page/index.html";
	}
	/**
	 * 绝对路径URL
	 * @return
	 */
	@RequestMapping("/loginhttp")
	public String redirectLogin() {
		return "redirect:http://localhost:8081/page/index.html";
	}
	
}
