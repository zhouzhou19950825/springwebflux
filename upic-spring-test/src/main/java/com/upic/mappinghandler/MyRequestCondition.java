package com.upic.mappinghandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.result.condition.RequestCondition;
import org.springframework.web.server.ServerWebExchange;

public  class MyRequestCondition implements RequestCondition<MyRequestCondition> {

	@Override
	public MyRequestCondition combine(MyRequestCondition other) {
		return this;
	}

	@Override
	public MyRequestCondition getMatchingCondition(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		MultiValueMap<String, String> queryParams = request.getQueryParams();
		List<String> list=new ArrayList<String>();
		list.add("1");
		queryParams.put("a", list);
		return this;
	}

	@Override
	public int compareTo(MyRequestCondition other, ServerWebExchange exchange) {
		return 0;
	}

}
