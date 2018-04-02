package com.upic.mappinghandler;

import java.lang.reflect.Method;

import org.springframework.web.reactive.result.condition.RequestCondition;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

public class MyRequestMappingHandler extends RequestMappingHandlerMapping {
	
	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		System.out.println("come on:"+method.getName());
//		return super.getCustomMethodCondition(method);
		return new MyRequestCondition();
	}
}
