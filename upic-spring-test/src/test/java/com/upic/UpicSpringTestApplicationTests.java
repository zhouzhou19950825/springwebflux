package com.upic;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpicSpringTestApplicationTests {
	private static List<String> words = Arrays.asList(
	        "the",
	        "quick",
	        "brown",
	        "fox",
	        "jumped",
	        "over",
	        "the",
	        "lazy",
	        "dog"
	        );
	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
//		 Flux<String> manyLetters = Flux
//			        .fromIterable(words)
//			        .flatMap(word -> Flux.fromArray(word.split("")))
//			        .distinct()
//			        .sort()
//			        .zipWith(Flux.range(1, Integer.MAX_VALUE),
//			              (string, count) -> String.format("%2d. %s", count, string));
//
//			  manyLetters.subscribe(System.out::println);
		System.out.println(new Date());
	}
}
