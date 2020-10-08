# Spring 관련

## Controller, RestController의 차이

### Controller

- Controller는 View를 함께 리턴할 수 있다. html 파일이나, 해당 마크업파일에 함께 렌더링 될 데이터 모델들을 함께 넣어서 리턴할 수 있다.
- Controller에서도 ResponseBody 어노테이션으로 데이터를 리턴할 수 있다.

사용자로부터 요청이 들어오면 이 요청은 DispatcherServlet으로 들어간다. 그 다음 Handler Mapping을 타서 적합한 Controller를 찾는다. 해당 Controller에서 View Resolver를 타서 적합한 View를 DispatcherServlet에서 Response로 리턴한다.

ResponseBody 어노테이션은 ViewResolver를 타지 않고 ResponseBody에 데이터를 넣어 리턴한다.

### RestController

- RestController는 ResponseBody가 붙어있다라고 생각하면 된다. 컨트롤러의 모든 응답이 ResponseBody에 담겨서 들어가게 된다. json/xml 형태로 데이터만 리턴하는 식으로 구현한다.

## Controller, Service, Component의 차이

Spring의 Controller, Service 어노테이션의 내용은 다음과 같다.

내용을 좀 보면 Controller는 classpath scan을 통해 auto detecting되고 RequestMapping 어노테이션 기반으로 된 핸들러 메서드를 일반적으로 사용한다.

Service는 DDD에서 말하는 Service라는 것을 명시한다.

```java
/**
 * Indicates that an annotated class is a "Controller" (e.g. a web controller).
 *
 * <p>This annotation serves as a specialization of {@link Component @Component},
 * allowing for implementation classes to be autodetected through classpath scanning.
 * It is typically used in combination with annotated handler methods based on the
 * {@link org.springframework.web.bind.annotation.RequestMapping} annotation.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 2.5
 * @see Component
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {

    /**
    * The value may indicate a suggestion for a logical component name,
    * to be turned into a Spring bean in case of an autodetected component.
    * @return the suggested component name, if any (or empty String otherwise)
    */
    @AliasFor(annotation = Component.class)
    String value() default "";

}

/**
 * Indicates that an annotated class is a "Service", originally defined by Domain-Driven
 * Design (Evans, 2003) as "an operation offered as an interface that stands alone in the
 * model, with no encapsulated state."
 *
 * <p>May also indicate that a class is a "Business Service Facade" (in the Core J2EE
 * patterns sense), or something similar. This annotation is a general-purpose stereotype
 * and individual teams may narrow their semantics and use as appropriate.
 *
 * <p>This annotation serves as a specialization of {@link Component @Component},
 * allowing for implementation classes to be autodetected through classpath scanning.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see Component
 * @see Repository
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

    /**
    * The value may indicate a suggestion for a logical component name,
    * to be turned into a Spring bean in case of an autodetected component.
    * @return the suggested component name, if any (or empty String otherwise)
    */
    @AliasFor(annotation = Component.class)
    String value() default "";

}
```
