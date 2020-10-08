# Bean 주입 방식과 순환 참조

Spring에서 Bean을 주입하는 방법을 세 가지가 있다.

1. Field Injection
2. Constructor Injection
3. Setter Injection

## Field Injection

실제 내 프로젝트에는 테스트 코드에서 WebTestClient 를 주입 받는 부분 밖에 필드 주입이 없다.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTemplate {

    @Autowired
    protected WebTestClient webTestClient;
```

이런 방식으로 주입을 받는 것이 필드 주입이다.

## Constructor Injection

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;
    private FormLoginAuthenticationProvider provider;

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFailureHandler,
                          FormLoginAuthenticationProvider provider) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.provider = provider;
    }

```

실제 내 프로젝트에서 살펴보면, 다음처럼 생성자에서 원하는 Bean들을 주입받는 Constrctor Injection 방식이 있다. 나머지 방식은 여러 단점들이 있고, 내가 선호하지 않아서 내 프로젝트 코드에는 없다. 그래서 단순 예시로만 살펴보자.

## Setter Injection

Setter Injection은 말 그대로 Setter로 주입을 받는다.

```java
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
```

해본적은 없지만 아마 이런 방식으로 주입을 받는 것 같다.

## 순환참조

각 Bean 주입 방식과 순환참조는 어떤 관계가 있을까? 일단 순환 참조에 대해 알아보자. 말 그대로 A bean에서 B를 주입받고, B bean에서는 A를 주입받는 것이다.

코드로 봐야 더 알아보기 쉬울 것 같다. 실제로 개발하면서 있었던 코드다.

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;
    private FormLoginAuthenticationProvider provider;

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFailureHandler,
                          FormLoginAuthenticationProvider provider) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.provider = provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    ...
}

-------

@Component
public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private UserRepository userRepository;

    public FormLoginAuthenticationProvider(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    ...
}

-------

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    ...
}
```

위 코드를 실행시키면 다음과 같은 순환 참조가 발생한다.

![Circular Reference](/2020/assets/img/circular_reference.png)

이런 상태를 순환 참조라고 한다.

## Bean 주입 방식과 순환 참조의 관계

생성자 주입 방식은 해당 생성자가 호출되는 시점에 빈을 주입받게 된다. 반면 필드, 세터 주입 방식은 객체를 생성 후에 해당 메서드를 이용하여 빈을 주입한다. 그래서 객체 생성 후 비즈니스 로직을 수행 중에 순환 참조 부분이 호출되면 순환 참조가 발생한다. 그 순간 그렇기 때문에 생성자 주입방식으로 빈을 주입 받으면, 순환 참조가 일어나게 된다.

그렇다고 해서 순환참조를 감추고자 필드, 세터 주입방식을 사용하자는 것이 아니라 오히려 생성자 주입 방식을 사용하여 순환 참조를 놓치지 않도록 해야한다.
