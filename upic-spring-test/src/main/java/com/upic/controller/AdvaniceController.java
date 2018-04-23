package com.upic.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.upic.po.User;

import reactor.core.publisher.Mono;
/**
 * 全局异常
 * @author dtz
 * @date 2018年4月23日
 * Email upicdtz@qq.com
 */
@ControllerAdvice
public class AdvaniceController {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<String> handle(ArithmeticException ex) {
		System.out.println("Come on:" + ex);
		ResponseEntity<String> r=new ResponseEntity<>("服务器异常",HttpStatus.INTERNAL_SERVER_ERROR);
		return r;
	}
	
	/**
	 * 测试是否全局的比局部的先执行？是的，并且局部的方法不会再被执行
	 * @param number
	 * @param model
	 * @return
	 */
	@ModelAttribute("user")
	// @ModelAttribute
	public Mono<User> addData(@RequestParam(required = false) String number, Model model) {
		return Mono.create(x -> {
			x.success(new User(1L, number+"123", "c", "d"));
		});
	}
	/**
	 * 全局先执行，局部的再执行
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
