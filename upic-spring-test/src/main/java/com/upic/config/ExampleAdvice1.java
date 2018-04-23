package com.upic.config;

/**
 * 默认@ControllerAdvice方法适用于每个请求，即所有控制器，但您可以通过注释上的属性将其缩小到控制器的子集
 * 
 * 启动时 全局作用的方法比局部方法执行前
 * @author dtz
 * @date 2018年4月24日 Email upicdtz@qq.com
 */
// Target all Controllers annotated with @RestController
// @ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {
}

// Target all Controllers within specific packages

// @ControllerAdvice(assignableTypes = {ControllerInterface.class,
// AbstractController.class})
class ExampleAdvice3 {
}

// Target all Controllers assignable to specific classes
// @ControllerAdvice("org.example.controllers")
class ExampleAdvice2 {
}
