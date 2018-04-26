package com.upic;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.upic.po.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpicSpringTestApplicationTests {
	private static List<String> words = Arrays.asList("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy",
			"dog");

	@Test
	public void contextLoads() {
		String uriTemplate = "http://localhost:8088/getResultBy";
		URI uri = UriComponentsBuilder.fromUriString(uriTemplate)
				// .queryParam("q", "{q}")
				// .buildAndExpand("Westin", "123")
				.build().encode().toUri();
		System.out.println("Authority = " + uri.getAuthority());
		System.out.println("Fragment = " + uri.getFragment());
		System.out.println("Host = " + uri.getHost());
		System.out.println("Path = " + uri.getPath());
		System.out.println("Port = " + uri.getPort());
		System.out.println("Query = " + uri.getQuery());
		System.out.println("Scheme = " + uri.getScheme());
		System.out.println("Scheme-specific part = " + uri.getSchemeSpecificPart());
		System.out.println("User Info = " + uri.getUserInfo());
		System.out.println("URI is absolute: " + uri.isAbsolute());
		System.out.println("URI is opaque: " + uri.isOpaque());

		// InputStream is = url.openStream ();

		// int ch;
		// while ((ch = is.read ()) != -1) {
		// System.out.print ((char) ch);
		// }
		// is.close ();
	}

	@Test
	public void testRest() {
		String uriTemplate = "http://localhost:8088/getUserDate";
		URI uri = UriComponentsBuilder.fromUriString(uriTemplate)
				// .queryParam("q", "{q}")
				.buildAndExpand("number", "123")
				// .build()
				.encode().toUri();
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(uriTemplate);
		factory.uriString(uriTemplate).queryParam("number", "14221101010101").build("Westin", "123");
		RestTemplate restTemplate = new RestTemplate();
		// restTemplate.setUriTemplateHandler(factory);
		// String forObject = restTemplate.getForObject(uri, String.class);
		String postForLocation = restTemplate.postForObject(uri, null, String.class);
		System.out.println(postForLocation);
	}

	public static void main(String[] args) {
		// Flux<String> manyLetters = Flux
		// .fromIterable(words)
		// .flatMap(word -> Flux.fromArray(word.split("")))
		// .distinct()
		// .sort()
		// .zipWith(Flux.range(1, Integer.MAX_VALUE),
		// (string, count) -> String.format("%2d. %s", count, string));
		//
		// manyLetters.subscribe(System.out::println);
		System.out.println(new Date());
	}
}
