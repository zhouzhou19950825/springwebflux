package com.upic;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import com.upic.po.CaseIn;
import com.upic.po.ResultData;

import reactor.core.publisher.Mono;
/**
 * 
 * @author dongtengzhou
 *
 */
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

	@Test
	public void testRetrive() {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("content", "print(\"1\")");
		maps.put("time", 5);

		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> string = client.post().uri("/pycheck", maps).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(ResultData.class);
		System.out.println(string.block());
	}

	@Test
	public void testError() {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("content", "print(\"1\")");
		maps.put("time", 5);
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> result = client.post().uri("/pycheck", maps).accept(MediaType.APPLICATION_JSON).retrieve()
				.onStatus(HttpStatus::is4xxClientError,
						response -> Mono
								.create(x -> x.success(new Exception("错误码:" + response.statusCode().value() + ""))))
				.onStatus(HttpStatus::is5xxServerError,
						response -> Mono
								.create(x -> x.success(new Exception("错误码:" + response.statusCode().value() + ""))))
				.bodyToMono(ResultData.class);
		System.out.println(result.block().toString());
	}

	/**
	 * test Exchanger 异常捕获需要自定义管理 code错误码管理
	 */
	@Test
	public void testExchanger() {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("content", "print(\"1\")");
		maps.put("time", 5);
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> result = client.post().uri("/pycheck", maps).accept(MediaType.APPLICATION_JSON).exchange()
				.timeout(Duration.ofSeconds(5)).flatMap(response -> response.bodyToMono(ResultData.class));
		System.out.println(result.block().toString());
	}

	@Test
	public void testExchanger1() {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("content", "print(\"1\")");
		maps.put("time", 5);
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResponseEntity<ResultData>> flatMap = client.post().uri("/pycheck", maps)
				.accept(MediaType.APPLICATION_JSON).exchange().timeout(Duration.ofSeconds(5))
				.flatMap(response -> response.toEntity(ResultData.class));
		System.out.println(flatMap.block().toString());
	}

	/**
	 * 测试通过模拟实体类请求
	 */
	@Test
	public void testRequestObj() {
		CaseIn in = new CaseIn();
		in.setContent("print(\"1\")");
		in.setTime(5L);
		Mono<CaseIn> create = Mono.create(x -> x.success(in));
		// Map<String, Object> maps = new HashMap<String, Object>();
		// maps.put("content", "print(\"1\")");
		// maps.put("time", 5);
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> string = client.post().uri("/pycheck").accept(MediaType.APPLICATION_JSON)
				.body(create, CaseIn.class).retrieve().bodyToMono(ResultData.class);
		System.out.println(string.block().toString());
	}

	/**
	 * 测试通过模拟form表单提交
	 */
	@Test
	public void testFormSub() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("content", "print(\"1\")");
		formData.add("time", 5 + "");
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> result = client.post().uri("/pycheck").syncBody(formData).retrieve()
				.bodyToMono(ResultData.class);
		System.out.println(result.block().toString());
	}

	@Test
	public void testFormSubBody() {
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> result = client.post().uri("/pycheck")
				.body(BodyInserters.fromFormData("content", "print(\"1\")").with("time", "5")).retrieve()
				.bodyToMono(ResultData.class);
		System.out.println(result.block().toString());
	}

	/**
	 * 测试Multipart多部分数据上传
	 */
	@Test
	public void testMultipart() {
		CaseIn in = new CaseIn();
		in.setContent("print(\"1\")");
		in.setTime(5L);
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("fieldPart", "fieldValue");
		builder.part("filePart", new FileSystemResource("...logo.png"));
		builder.part("jsonPart", in);

		MultiValueMap<String, HttpEntity<?>> parts = builder.build();
		WebClient client = WebClient.create("http://192.144.140.169:8081");
		Mono<ResultData> result = client.post().uri("/pycheck")
				.syncBody(parts).retrieve()
				.bodyToMono(ResultData.class);
		System.out.println(result.block().toString());
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
