package com.upic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;

import com.upic.po.User;

/**
 * 在第一个请求中，当名称为“pet”的模型属性添加到模型中时，它会自动提升并保存在模型中WebSession。
 * 直到另一个控制器方法使用SessionStatus方法参数清除存储时，它仍然存在
 * 
 * @author dtz
 * @date 2018年4月9日 Email upicdtz@qq.com
 */
@Controller
@SessionAttributes({ "path", "user" })
public class EditPetForm {
	/**
	 * webflux为啥不能重定向
	 * model传递给下一个方法，需要在@SessionAttributes申明字段（键）
	 * @param user
	 * @param errors
	 * @param status
	 * @return
	 */
	@GetMapping("/user/{id}")
	public String handle(User user, BindingResult errors, SessionStatus status, Model model) {
		if (errors.hasErrors()) {
			// ...
			// return "error";
		}
		// model.addAttribute("users", user);
		// status.setComplete();
		// ...
		model.addAttribute("user", user);
//		Rendering build = Rendering.redirectTo("/complete").build();
//		return build;
		return "redirect:/complete";
	}

	/**
	 * 获取重定向缓存方法
	 * 1、通过model接收
	 * 2、通过@SessionAttribute(name) 接收
	 * @param model
	 * @param status
	 * @param user
	 * @return
	 */
	@RequestMapping("/complete")
	@ResponseBody
	public User complete(Model model, SessionStatus status, @SessionAttribute("user") User user) {
		status.setComplete();
		return (User) model.asMap().get("user");
	}

	@RequestMapping("/redirectTest")
	public Rendering redirectTest(Model model) {
		model.addAttribute("path", "mypath");
		return Rendering.redirectTo("/indexView").build();
	}

	@RequestMapping("/indexView")
	@ResponseBody
	public String indexView(Model modelMap, SessionStatus sessionStatus) {
		System.out.println(modelMap.asMap());
		sessionStatus.setComplete(); // 这一样必须要有
		return modelMap.asMap().toString();
	}
}
