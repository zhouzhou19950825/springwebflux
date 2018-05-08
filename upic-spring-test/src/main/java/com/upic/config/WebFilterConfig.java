package com.upic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

import com.upic.filter.TestFilter;
@Configuration
public class WebFilterConfig {

	@Bean
	public WebFilter getFilter() {
		return new TestFilter();
	}
}
