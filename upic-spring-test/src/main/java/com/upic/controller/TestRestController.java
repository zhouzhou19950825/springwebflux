package com.upic.controller;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.upic.po.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
//@CrossOrigin(maxAge = 3600,methods= {RequestMethod.PUT})
@RestController
public class TestRestController {

	@GetMapping("/hello")
	public String handle() {
		return "Hello WebFlux";
	}

	@GetMapping("/{id}")
	public String returnChar(@PathVariable String id) {
		return id;
	}

	/**
	 * Spring WebFlux不支持后缀模式匹配 - 与Spring MVC不同，后者的映射/person也与之匹配/person.*
	 * 
	 * 但是测试之后发现也是可行的，这个是个官方文档可能出现的一个问题
	 */
	@GetMapping("/person.*")
	public String getPerson() {
		return "a";
	}

	@GetMapping("/getA")
	public String getA(String a) {
		return a;
	}

	/**
	 * @RequestParam
	 * @param id
	 * @param username
	 * @return
	 */
	@GetMapping("/getUpic")
	public String setupForm(String id, @RequestParam("user") String username) {
		return id + username;
	}

	/**
	 * http://localhost:8088/getUpics?id=5&user=dongshuai
	 * 
	 * @param maps
	 * @return MultiValueMap<String, String> Map<String,String>maps
	 * 
	 *         以上两种类型被@RequestParam修饰，所有的参数会以键值对被注入
	 */
	@GetMapping("/getUpics")
	public Map<String, String> setupForm(@RequestParam Map<String, String> maps) {
		return maps;
	}

	/**
	 * 获取头信息
	 * 
	 * @param encoding
	 * @param keepAlive
	 */
	@GetMapping("/demoHandle")
	public String handle(@RequestHeader("Accept-Encoding") String encoding,
			@RequestHeader("Keep-Alive") long keepAlive) {
		return encoding + keepAlive;
	}

	/**
	 * http://localhost:8088/demoHandles 当@RequestHeader注解上的使用Map<String, String>，
	 * MultiValueMap<String, String>或HttpHeaders参数，则map被填充有所有标头值
	 * 
	 * @param maps
	 * @return
	 */
	@GetMapping("/demoHandles")
	public Map<String, String> handleList(@RequestHeader Map<String, String> maps) {
		return maps;
	}

	/**
	 * 带注释的方法参数@RequestHeader("Accept")可以是类型的 String，但也String[]还是List<String>。
	 * 
	 * @param accept
	 * @return
	 */
	@GetMapping("/demoAccept")
	public String handleAccept(@RequestHeader("Accept") String accept) {
		return accept;
	}

	@GetMapping("/demoAcceptArray")
	public String[] handleAcceptArray(@RequestHeader("Accept") String[] accept) {
		return accept;
	}

	@GetMapping("/demoAcceptList")
	public List<String> handleAcceptList(@RequestHeader("Accept") List<String> accept) {
		return accept;
	}

	@GetMapping("/getcookie")
	public String handle(@CookieValue("JSESSIONID") String cookie) {
		return cookie;
	}

	/**
	 * @ModelAttribute 对象属性匹配 如果对象未标注（不是基本数据类型），默认为标注
	 * @param user
	 * @return
	 */
	@GetMapping("/user/{id}/users/{stuName}/edit")
	// public User processSubmit(@ModelAttribute User user) {
	public User processSubmit(User user) {
		return user;
	}

	/**
	 * 字段匹配异常错误会注入到BindingResult result对象
	 * 
	 * @param pet
	 * @param result
	 * @return
	 */
	@GetMapping("/owners/{id}/pets/{stuName}/edit/error")
	public String processSubmit(@ModelAttribute User pet, BindingResult result) {
		if (result.hasErrors()) {
			return result.getAllErrors().toString();
		}
		return pet.toString();
	}

	/**
	 * url:http://localhost:8088/user/1/pets/a/edit?id=aa&stuName=dtz //error Spring
	 * WebFlux支持模型中的反应类型，例如 Mono<Account>或io.reactivex.Single<Account>
	 * 
	 * @param petMono
	 * @return
	 */
	@GetMapping("/user/{id}/pets/{stuName}/edit")
	public Mono<String> processSubmit(@ModelAttribute("user") Mono<User> petMono) {
		return petMono.map(x -> x.toString()).onErrorResume(ex -> {
			return Mono.create(x -> x.success(ex.getMessage()));
		});
	}

	/**
	 * url:http://localhost:8088/user/edit?id=aa&stuName=dtz
	 * 测试BindingResult在Mono修饰的时候是否有用
	 * 
	 * @param petMono
	 * @param result
	 * @return
	 */
	// error 无法注入Failed to resolve argument 1 of type
	// 'org.springframework.validation.BindingResult
	@GetMapping("/user/edit")
	public Mono<String> processSubmit01(@ModelAttribute("user") Mono<User> petMono, BindingResult result) {
		if (result.hasErrors()) {
			return Mono.create(x -> x.success(result.getAllErrors().toString()));
		}
		return petMono.map(x -> x.toString()).onErrorResume(ex -> {
			return Mono.create(x -> x.success(ex.getMessage()));
		});
	}

	// /**
	// *
	// * @param user
	// * @return
	// */
	// @GetMapping("/user/getSessionUser")
	// public Object returnUser(ModelMap model) {
	// System.out.println(model.get("user"));
	// return model.get("user");
	// }
	/**
	 * @SessionAttribute WebSession
	 * @param user
	 * @param session
	 * @return
	 */
	@GetMapping("/setusersession")
	public User addUserToSession(User user) {
		return user;
	}

	// @GetMapping("/getUserSession")
	// public User handle(@SessionAttribute User user) {
	// return user;
	// }

	/**
	 * 测试文件上传 需要第三方库来解析
	 * 
	 * @param form
	 * @param errors
	 * @return
	 * @throws IOException
	 *             MyForm form
	 */
	// @PostMapping("/form")
	// public String handleFormUpload(MultipartFile file, BindingResult errors)
	// throws IOException {
	// // ...
	// try (BufferedOutputStream stream = new BufferedOutputStream(
	// new FileOutputStream(new File("img/" + file.getOriginalFilename())));) {
	// byte[] inputStream = file.getBytes();
	// stream.write(inputStream);
	// } catch (Exception e) {
	// }
	// return "success";
	// }
	//
	/**
	 * webflux文件分解在一个个Part当中->FilePart等
	 * 
	 * @param metadata
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/formdata")
	public String handle(@RequestPart(value = "meta-data", required = false) Part metadata,
			@RequestPart("file") FilePart file) throws IOException {
		// ...
		System.out.println(file.filename());
		Path tempFile = Files.createTempFile("test", file.filename());

		// NOTE 方法一
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
		DataBufferUtils.write(file.content(), channel, 0).doOnComplete(() -> {
			System.out.println("finish");
		}).subscribe();
		// NOTE 方法二
		// filePart.transferTo(tempFile.toFile());

		System.out.println(tempFile.toString());
		return file.filename();
	}

	@GetMapping("/download")
	public Mono<Void> downloadByWriteWith(ServerHttpResponse response) throws IOException {
		ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
		response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=parallel.png");
		response.getHeaders().setContentType(MediaType.IMAGE_PNG);

		Resource resource = new ClassPathResource("parallel.png");
		File file = resource.getFile();
		return zeroCopyResponse.writeWith(file, 0, file.length());
	}

	/**
	 * 要反序列化原始部分内容，例如JSON（类似于@RequestBody），只需声明一个具体的目标Object，而不是Part
	 * 
	 * @param user
	 * @return
	 */
	// @PostMapping("/partUser")
	// public String handleReq(@RequestPart("user") Part user/User user) { //It's
	// error can not parser
	// // ...
	// return user.toString();
	// }
	@PostMapping("/partUser")
	public String handleReq(User user) { // default @RequestBody
		// ...
		return user.toString();
	}

	/**
	 * @RequestPart可以结合使用javax.validation.Valid，或Spring的 @Validated注释，
	 *                                                   这会导致应用标准Bean验证。默认情况下，验证错误会导致一个WebExchangeBindException变成400（BAD_REQUEST）响应。
	 *                                                   或者，验证错误可以通过Errors或BindingResult参数在控制器内本地处理
	 *                                                   Part:字段属性存储单位对象，存储在MultiValueMap<String,
	 *                                                   Part>对象当中
	 * @param user
	 * @param result
	 * @return
	 */
	@PostMapping("/handleValid")
	public Flux<String> handleValid(@Valid @RequestPart("test") Part part) {
		Flux<DataBuffer> content = part.content();
		return Flux.create(x -> {
			x.next(content.toStream().map(m -> new String(m.asByteBuffer().array())).findFirst().get());
			x.complete();
		});
		// ...
		// return part.name();
	}

	/**
	 * webflux 多参数封装
	 * 
	 * @param parts
	 * @return
	 */
	@PostMapping("/handleMultiPart")
	public Map<String, String> handleMultiPart(@RequestBody Mono<MultiValueMap<String, Part>> parts) {
		// ...
		Map<String, String> maps = new HashMap<>();
		MultiValueMap<String, Part> block = parts.block();
		block.forEach((x, y) -> {
			System.out.println("key:" + x);
			System.out.println("value:" + y.size());
			System.out.println("value:" + y.get(0));
			// y.get(0).content().toStream().map(m -> new
			// String(m.asByteBuffer().array())).findFirst().get());
			Stream<DataBuffer> stream = y.get(0).content().toStream();
			List<String> collect = stream.map(m -> new String(m.asByteBuffer().array())).collect(Collectors.toList());
			// collect.stream().forEach(System.out::println);
			maps.put(x, collect.get(0));
		});
		return maps;
	}

	// @PostMapping("/handleMultiPart")
	// public MultiValueMap<String, Part> handleMultiPart(@RequestBody
	// MultiValueMap<String, Part> parts) {
	// // ...
	//// MultiValueMap<String, Part> block = parts.block();
	// return parts;
	// }
	@PostMapping("/handleFlux")
	public List<Map<String, List<String>>> handleFlux(@RequestBody Flux<Part> parts) {
		List<Part> block = parts.collectList().block();
		List<Map<String, List<String>>> list = new ArrayList<>();
		block.forEach(x -> {
			List<String> collect = x.content().toStream().map(m -> new String(m.asByteBuffer().array()))
					.collect(Collectors.toList());
			Map<String, List<String>> maps = new HashMap<>();
			maps.put(x.name(), collect);
			list.add(maps);
		});
		return list;
	}

	/**
	 * 使用@RequestBody注释通过HttpMessageReader将请求主体读取并反序列化成Object
	 * 
	 * @param user
	 * @return
	 */
	@GetMapping("/geusers")
	public User handle(@RequestBody User user) {
		return user;
	}

	/**
	 * 与Spring MVC不同的是，在WebFlux中，@RequestBody方法参数支持响应类型和完全非阻塞读取和（客户端到服务器）流
	 * 
	 * @param account
	 */
	@PostMapping("/accounts")
	public User handle(@RequestBody Mono<User> user) {
		return user.block();
	}

	@PostMapping("/geusersreserror")
	public String handle(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			return result.getAllErrors().toString();
		}
		return user.toString();
	}

	@PostMapping("/entity")
	public String handleHttpEntity(HttpEntity<User> entity) {
		// ...
		return entity.toString();
	}

	@GetMapping("/user")
	// @JsonView(User.father.class)
	@JsonView(User.son.class)
	public User getUser() {
		return new User(1L, "a", "eric", "7!jd#h23");
	}

	/**
	 * @ModelAttribute ：修饰在方法上执行在requestMapping之前，初始化数据放在model中 model 存储多个数据
	 * @param number
	 * @param model
	 */
	// @ModelAttribute
	// @GetMapping("getModel")
	// public void populateModel(@RequestParam String number, Model model) {
	// model.addAttribute(accountRepository.findAccount(number));
	// model.addAttribute(new User(1L,"number","a","b"));
	// add more ...
	// }

	/**
	 * 增加单个数据
	 * 
	 * @param number
	 * @return
	 */
	// @ModelAttribute("user")
	// public User addAccount(@RequestParam String number) {
	// return new User(1L,number,"a","b");
	// }

	/**
	 * 
	 * @param numbber
	 * @param model
	 * @return
	 */
	@GetMapping("getPreModel")
	public User getUser(String numbber, Model model) {
		return (User) model.asMap().get("user");
	}

	/**
	 * 此外，任何具有反应型包装的模型属性都会在视图呈现之前解析为其实际值（并更新模型）。 不知道为什么 Post不行
	 * 
	 * @param number
	 * @param model
	 * @return
	 */
	@ModelAttribute("user")
	// @ModelAttribute
	public Mono<User> addData(@RequestParam(required = false) String number, Model model) {
		return Mono.create(x -> {
			x.success(new User(1L, number, "c", "d"));
		});
		// model.addAttribute("user", accountMono);
	}

	@ModelAttribute("user")
	public Mono<User> addData2(@RequestParam(required = false) String number, Model model) {
		return Mono.create(x -> {
			x.success(new User(1L, number, "c", "d"));
		});
		// model.addAttribute("user", accountMono);
	}

	// @ModelAttribute("user")
	// public Mono<User> addData1(@RequestParam Long id, Model model) {
	// return Mono.create(x -> {
	// x.success(new User(id, "1422110108", "c", "d"));
	// });
	// // model.addAttribute("user", accountMono);
	// }
	/**
	 * POST请求不行
	 * 
	 * @param user
	 * @param errors
	 * @return
	 */
	@PostMapping("/getMonoModel")
	public User handles(@ModelAttribute("user") User user, BindingResult errors) {
		if (errors.hasErrors()) {

		}
		return user;
	}

	/**
	 * @ModelAttribute 也可以用作方法的方法级注释，@RequestMapping
	 *                 在这种情况下方法的返回值@RequestMapping被解释为模型属性。这通常不是必需的，
	 *                 因为它是HTML控制器中的默认行为，除非返回值是否String会被视为视图名称。 @ModelAttribute也可以帮助定制模型属性名称：
	 */
	@GetMapping("/accounts/{id}")
	@ModelAttribute("/page/index.html")
	public User handleUser(@PathVariable Long id) {
		// ...
		return new User(id, "1422110108", "c", "d");
	}

	/**
	 * 他的执行顺序比全局的后，但是比其他全局或者局部的其他方法先
	 * 全局的先执行，然后再执行局部@InitBinder
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	/**
	 * 当Formatter通过共享使用基于设置的设置时
	 * FormattingConversionService，您可以重新使用相同的方法并注册控制器特定Formatter的设置
	 * 
	 * @param binder
	 * @return
	 */
	// @InitBinder
	// protected void initBinder(WebDataBinder binder) {
	// binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	// }

	/**
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/getUserDate")
	public User handleUserDate(User user) {
		// ...
		return user;
	}
	/**
	 * 测试异常
	 * @return
	 */
	@GetMapping("/getResultBy")
	public Long returnData() {
		return 1L/0;
	}
	
	 
}
