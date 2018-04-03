package com.upic.controller;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 矩阵请求
 * 想要使用举证请求
 * 必须将RequestMappingHandlerMapping 中removeSemicolonContent设置为false
 * {@link}com.upic.config.WebConfig
 * 
 * 与springmvc有所不同 矩阵参数可以比接受参数多 而springMvc则不允许
 * @author dtz
 * @date 2018年4月3日 Email upicdtz@qq.com
 */
@RestController
public class MatrixController {

	/**
	 * GET /pets/42;q=11;r=22
	 * require=false
	 * defaultValue 默认值
	 * @return
	 */
	@GetMapping("/pets/{petId}")
	public String getMatrix(@PathVariable String petId, @MatrixVariable(name="q",required=false,defaultValue="1") int q) {
		System.out.println(petId + ":" + q);
		return petId + ":" + q;
	}

	/**
	 * GET /owners/42;q=11/pets/21;q=22
	 * 
	 * @param q1
	 * @param q2
	 */
	@GetMapping("/owners/{ownerId}/pets/{petId}")
	public String findPet(@MatrixVariable(name = "m", pathVar = "ownerId") int q1,
			@MatrixVariable(name = "m", pathVar = "petId") int q2) {
		// q1 == 11
		// q2 == 22
		System.out.println(q1+"&"+q2);
		return q1+"&"+q2;
	}
	
	
	// GET /owners/42;q=11;r=12/pets/21;q=22;s=23

	@GetMapping("/owners/{ownerId}/pets/{petId}/a")
	public String findPet(
	        @MatrixVariable MultiValueMap<String, String> matrixVars,
	        @MatrixVariable(pathVar="petId") MultiValueMap<String, String> petMatrixVars) {

	    // matrixVars: ["q" : [11,22], "r" : 12, "s" : 23]
	    // petMatrixVars: ["q" : 22, "s" : 23]
		
		return matrixVars.toString()+petMatrixVars.toString();
	}
}
