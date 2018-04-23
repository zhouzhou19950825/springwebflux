package com.upic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 一些需要解决的问题：
 * 
 * 尝试使用Spring Boot依赖管理而不是重写Spring项目版本
 * 添加spring-boot-starter-web或spring-boot-starter-websocket使您的项目成为Spring
 * MVC应用程序，这不是您想要的。删除这些依赖关系 删除@EnableWebFlux，因为它禁用Spring Boot中的WebFlux自动配置
 * 删除@EnableWebFluxSecurity，因为它已经由Spring Boot完成了
 * 
 * 
 * spring webflux如果访问url不存在，则返回请求的url路径
 * @author dtz
 * @date 2018年4月9日 Email upicdtz@qq.com
 */
@SpringBootApplication
// @EnableWebFluxSecurity
// @EnableWebFlux
public class UpicSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpicSpringTestApplication.class, args);
//		System.out.println(1/0);
	}

}
