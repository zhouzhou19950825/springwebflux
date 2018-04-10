# springwebflux
针对spring webflux相关技术


# Spring-webflux redirect problem

>请看com.upic.controller.EditPetForm

```
@GetMapping("/user/{id}")
	public String handle(User user, BindingResult errors, SessionStatus status, Model model) {
		if (errors.hasErrors()) {
			// ...
			// return "error";
		}
		// model.addAttribute("users", user);
		// status.setComplete();
		// ...
		model.addAttribute("user", user);
//		Rendering build = Rendering.redirectTo("/complete").build();
//		return build;
		return "redirect:/complete";
	}
	@RequestMapping("/complete")
	@ResponseBody
	public User complete(Model model, SessionStatus status, @SessionAttribute("user") User user) {
		status.setComplete();
		return (User) model.asMap().get("user");
	}
```
>I want to test a redirect and model cache,but error:

```
java.lang.IllegalStateException: Could not resolve view with name 'redirect:/complete'.
	at org.springframework.web.reactive.result.view.ViewResolutionResultHandler.lambda$resolveViews$3(ViewResolutionResultHandler.java:277) ~[spring-webflux-5.0.4.RELEASE.jar:5.0.4.RELEASE]
	at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:107) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onNext(FluxOnAssembly.java:450) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1069) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoCollectList$MonoBufferAllSubscriber.onComplete(MonoCollectList.java:117) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:460) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.drain(FluxConcatMap.java:349) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.onComplete(FluxConcatMap.java:265) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:460) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.Operators.complete(Operators.java:125) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxIterable.subscribe(FluxIterable.java:111) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxIterable.subscribe(FluxIterable.java:61) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxOnAssembly.subscribe(FluxOnAssembly.java:252) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxConcatMap.subscribe(FluxConcatMap.java:121) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.FluxOnAssembly.subscribe(FluxOnAssembly.java:252) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoCollectList.subscribe(MonoCollectList.java:59) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoOnAssembly.subscribe(MonoOnAssembly.java:76) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoMapFuseable.subscribe(MonoMapFuseable.java:59) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoOnAssembly.subscribe(MonoOnAssembly.java:76) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoFlatMap.subscribe(MonoFlatMap.java:60) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoOnAssembly.subscribe(MonoOnAssembly.java:76) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:150) ~[reactor-core-3.1.5.RELEASE.jar:3.1.5.RELEASE]
```

>Because my string return is treated as view forwarding，By looking at the source code 
>`org.springframework.web.reactive.result.view.ViewResolutionResultHandler` class. Finding the method in the handleResultResult:

```
...
else if (CharSequence.class.isAssignableFrom(clazz) && !hasModelAnnotation(parameter))

{ viewsMono = resolveViews(returnValue.toString(), locale); }
else if (Rendering.class.isAssignableFrom(clazz)) {
Rendering render = (Rendering) returnValue;
HttpStatus status = render.status();
if (status != null)

{ exchange.getResponse().setStatusCode(status); }
exchange.getResponse().getHeaders().putAll(render.headers());
model.addAllAttributes(render.modelAttributes());
Object view = render.view();
if (view == null)

{ view = getDefaultViewName(exchange); }
...
```
Discovered string is forwarded as view name.

So I rewritten this class：

```
	if (returnValue.toString().startsWith(REDIRECT_TAG)) {
		Rendering render =Rendering.redirectTo(returnValue.toString().substring(REDIRECT_TAG.length(),returnValue.toString().length() )).build();
					viewsMono=assembled(render, exchange, model, locale);
				} else {
					viewsMono = resolveViews(returnValue.toString(), locale);
				}
```
>Please download my code verification, thanks!
