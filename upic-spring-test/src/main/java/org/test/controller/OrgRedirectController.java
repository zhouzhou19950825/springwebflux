package org.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
/**
 * 由于不在根目录下，需要配置才能访问
 * @author dongtengzhou
 *@see com.upic.config.ControllerConfig
 */
@Controller
public class OrgRedirectController {

	/**
	 * 重定向，通过返回字符串
	 * @return
	 */
	@RequestMapping("/loginorg")
	public String login() {
		return "redirect:/page/index.html";
	}
	/**
	 * 绝对路径URL
	 * @return
	 */
	@RequestMapping("/loginhttporg")
	public String redirectLogin() {
		return "redirect:http://localhost:8081/page/index.html";
	}
	
}
