# Spring Security Architecture - 1

## DelegatingFilterProxy

Servlet Filter는 스프링에서 정의된 빈을 주입해서 사용할 수 없다. 그래서..

1. DelegatingFilterProxy는 Servlet Filter로 들어온 요청을 Spring에서 관리하는 Bean에 요청을 위임한다.
    - springSecurityFilterChain 이름으로 생성된 빈을 ApplicationContext에서 찾아서 요청을 **위임**한다.(실제 보안처리를 하지는 않는다.)

## FilterChainProxy

1. SpringSecurityFilterChain 이름으로 생성되는 bean이고 DelegatingFilterProxy로부터 요청을 위임받고 실제 보안처리를 한다.
2. 기본적으로 스프링 시큐리티가 생성하는 필터 + 설정 클래스에서 생성되는 필터
3. Request를 Filter chain 순서대로 전달한다.
4. 커스텀 필터로 기존 필터 전, 후로 추가 가능하다.
5. 마지막 필터까지 인증, 인가 예외 발생하지 않으면 통과

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .cors();

    http
            .addFilterBefore(kakaoLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(githubLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class);
}
```

위와 같은 방식으로 필터 추가 가능하다.

## Configuration Class

1. 설정 클래스는 여러 개 만들 수도 있다.(extends WebSecurityConfigurerAdapter)
2. 설정 클래스 별로 각각 작동
3. 설정 클래스 별로 RequestMatcher 설정
4. 설정 클래스 별로 필터가 생성되고, FilterChainProxy가 이 필터들을 가지고 있다.(FilterChains)
5. 사용자 요청이 RequestMatcher와 매칭되는 필터가 작동

FilterChainProxy 클래스는 다음과 같이 생겼다.

```java
public class FilterChainProxy extends GenericFilterBean {
    // ~ Static fields/initializers
    // =====================================================================================

    private static final Log logger = LogFactory.getLog(FilterChainProxy.class);

    // ~ Instance fields
    // ================================================================================================

    private final static String FILTER_APPLIED = FilterChainProxy.class.getName().concat(
            ".APPLIED");

    private List<SecurityFilterChain> filterChains;

    private FilterChainValidator filterChainValidator = new NullFilterChainValidator();

    private HttpFirewall firewall = new StrictHttpFirewall();

    // ~ Methods
    // ========================================================================================================

    public FilterChainProxy() {
    }

    public FilterChainProxy(SecurityFilterChain chain) {
        this(Arrays.asList(chain));
    }

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

...
```

설정 파일을 여러 개로 나누더라도 RequestMatcher만 올바르게 나누어주면 사용자 요청에 맞게 필터를 적용할 수 있다. 그러나 설정의 RequestMatcher를 좁은 범위부터 먼저 적용시켜야 한다.(넓은 범위의 RequestMatcher가 먼저 적용되면 뒤의 것은 적용이 안된다.)
