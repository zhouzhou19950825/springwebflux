package com.upic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.result.view.Rendering;

import com.upic.po.User;

/**
 * 在第一个请求中，当名称为“pet”的模型属性添加到模型中时，它会自动提升并保存在模型中WebSession。
 * 直到另一个控制器方法使用SessionStatus方法参数清除存储时，它仍然存在
 * 
 * @author dtz
 * @date 2018年4月9日 Email upicdtz@qq.com
 */
@Controller
@SessionAttributes("path")
public class EditPetForm {
/**
 * webflux为啥不能重定向
 * @param user
 * @param errors
 * @param status
 * @return
 */

	@GetMapping("/user/{id}")
//	@ResponseBody
	public String handle(User user,BindingResult errors, SessionStatus status) {
		if (errors.hasErrors()) {
			// ...
			return "error";
		}
//		model.addAttribute("users", user);
//		status.setComplete();
		// ...
		Rendering.redirectTo("/complete").build();
		return "redirect:/complete";
	}
	@RequestMapping("/complete")
	@ResponseBody
	public User complete(@SessionAttribute User user, SessionStatus status) {
		status.setComplete();
		return user;
	}
	@RequestMapping("/redirectTest")
	public String redirectTest(Model model){
	    model.addAttribute("path","mypath");
	    return "redirect:indexView";
	}

	@RequestMapping("/indexView")
	@ResponseBody
	public String indexView(ModelMap modelMap, SessionStatus sessionStatus){
	    System.out.println(modelMap.get("path"));
	    sessionStatus.setComplete();  //这一样必须要有
	    return modelMap.get("path").toString();
	}
}
