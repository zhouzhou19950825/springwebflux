package com.upic.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * 异常优先级测试，这里的异常只适用于当前controller,不是全局异常
 * HandlerAdapter 视图解析、适配器
 * @author dtz
 * @date 2018年4月23日
 * Email upicdtz@qq.com
 */
@Controller
public class ExceptionController {
	
	/**
	 * 可进行参数匹配，先精准匹配，在进行可转型匹配
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<String> handle(ArithmeticException ex) {
		System.out.println("Come on:" + ex);
		return null;
	}
	
	@ExceptionHandler
	public ResponseEntity<String> handle(Exception ex) {
		System.out.println("Come on:" + ex);
		return null;
	}
	
//	@GetMapping("/getResultBy")
//	public Long returnData() {
//		return 1L/0;
//	}
}
