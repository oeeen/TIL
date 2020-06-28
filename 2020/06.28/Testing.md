# 19. Testing

## 19.1.1. Security Test Setup

스프링 시큐리티 테스트를 사용하기 전에 아래 나온 것처럼 사전 설정을 해야한다.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class WithMockUserTests {
```

공식 문서에는 위처럼 나와있지만, 내 프로젝트에는 아래처럼 적용했다. `@RunWith(SpringJUnit4ClassRunner.class)`부분은 Junit5에서 `@ExtendWith(SpringExtension.class)`로 사용할 수 있고 ContextConfiguration 설정을 해주었다.

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CommerceApplication.class, H2ConsoleProperties.class})
@WebAppConfiguration
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
```

## 19.1.2. @WithMockUser

문제는 "특정 사용자로서 테스트를 어떻게 할까?" 이다. 답은 `@WithMockUser`를 사용하는 것이다. 아래 테스트는 username "user"와 password "password"인 "ROLE_USER" 권한으로 테스트를 돌린다.

```java
@Test
@WithMockUser
public void getMessageWithMockUser() {
String message = messageService.getMessage();
...
}
```

다음과 같은 내용이 뒷받침된다.

1. "user"이름을 가진 유저는 없어도 된다.(mocking하기 때문에)
2. UsernamePasswordAuthenticationToken 타입의 Authentication 객체가 SecurityContext 내에 생긴다.
3. Authentication 객체 내의 principal은 Spring Security의 User 객체이다.
4. User는 username은 "user", password는 "password", 권한은 "ROLE_USER"이다.

만약 다른 사용자 이름을 사용하고 싶다면 다음처럼 하면 된다.

```java
@Test
@WithMockUser("customUsername")
public void getMessageWithMockUserCustomUsername() {
    String message = messageService.getMessage();
...
}
```

또한 권한도 쉽게 커스터마이징 할 수 있다.

```java
@Test
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public void getMessageWithMockUserCustomUser() {
    String message = messageService.getMessage();
    ...
}
```

기본 설정으로 SecurityContext는 TestExecutionListener.beforeTestMethod 이벤트 동안 설정된다. 이는 JUnit의 @Before 이전에 일어나는 것과 같다. 이 방식에서 @Before를 실행한 이후지만 테스트 메서드가 실행되기 전에 SecurityContext가 설정되도록 변경할 수 있다. (`@WithMockUser(setupBefore = TestExecutionEvent.TEST_EXECUTION)`)

## 19.1.3. @WithAnonymousUser

@WithAnonymousUser를 사용하는 것은 anonymous 사용자로 테스트를 돌리는 것이다. 이 방식은 대부분은 특정 유저로 테스트하고, 일부분의 테스트만 anonymous로 하려고 할 때 특히 유용하다. 예를 들어 아래 나오는 내용은 withMockUser1과 withMockUser2 메서드는 @WithMockUser로 돌리고, anonymous 메서드는 anonymous 사용자로 테스트를 돌린다.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@WithMockUser
public class WithUserClassLevelAuthenticationTests {

    @Test
    public void withMockUser1() {
    }

    @Test
    public void withMockUser2() {
    }

    @Test
    @WithAnonymousUser
    public void anonymous() throws Exception {
        // override default to run as anonymous user
    }
}
```

위에서 말했던 것과 동일하게 @Before 이후 ~ 테스트 메서드 실행 전에 SecurityContext를 설정하도록 변경할 수 있다. (`@WithAnonymousUser(setupBefore = TestExecutionEvent.TEST_EXECUTION`)

## 19.1.4. @WithUserDetails

@WithMockUser가 시작하기에 편하지만, 모든 인스턴스에서 동작하는 것은 아니다. 예를 들어 어플리케이션이 특정한 타입의 Authentication principal을 기대하는 것이 일반적이다. 이는 어플리케이션이 principal을 커스텀 타입으로 지정하고 스프링 시큐리티와 커플링을 줄일 수 있도록 한다.

커스텀 principal은 UserDetails와 커스텀 타입을 모두 구현하는 객체를 리턴하는 커스텀 UserDetailsService에서 종종 리턴된다. 이런 상황에서 Custom UserDetailsService를 사용해서 테스트 용 유저를 만드는 것이 유용하다. 이게 @WithUserDetails가 정확히 하는 일이다.

UserDetailsService가 빈으로 등록되어 있고, 다음 테스트가 UsernamePasswordAuthenticationToken 타입의 인증 객체와 UserDetailsService를 통해 나온 principal과 함께 호출될 것이다.

```java
@Test
@WithUserDetails
public void getMessageWithUserDetails() {
    String message = messageService.getMessage();
    ...
}
```

커스텀 사용자 이름을 사용하려면 다음처럼 하면 된다.

```java
@Test
@WithUserDetails("customUsername")
public void getMessageWithUserDetailsCustomUsername() {
    String message = messageService.getMessage();
    ...
}
```

우리는 UserDetailsService를 특정 이름으로 찾을 수 있다. 아래 나온 것 처럼 작성하면 customUsername을 가진 사용자를 myUserDetailsService를 bean name으로 갖는 UserDetailsService를 통해 얻는다.

```java
@Test
@WithUserDetails(value="customUsername", userDetailsServiceBeanName="myUserDetailsService")
public void getMessageWithUserDetailsServiceBeanName() {
    String message = messageService.getMessage();
    ...
}
```

@WithMockUser 처럼 annotation을 클래스 레벨에도 달 수 있다. 그러나 @WithMockUser와는 다르게 @WithUserDetails는 실제 유저가 존재해야한다.

## 19.1.5. @WithSecurityContext

우리는 @WithSecurityContext를 이용해서 우리가 원하는 SecurityContext를 만들 수 있는 커스텀 어노테이션을 만들 수 있다. 예를 들어 우리는 @WithMockCustomUser라는 어노테이션을 아래 나온 것처럼 만들 수 있다.

```java
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "rob";

    String name() default "Rob Winch";
}
```

@WithMockCustomUser는 @WithSecurityContext 어노테이션과 함께 사용되었음을 볼 수 있다. 이는 Spring Security Test support에게 우리가 테스트에서 SecurityContext를 만들기 위해 신호를 주는 것이다. @WithSecurityContext 어노테이션은 @WithMockCustomUser에게 새로운 SecurityContext를 제공하기 위한 SecurityContextFactory를 특정하는 것이 필요하다. SecurityContextFactory를 아래처럼 구현하는 것을 볼 수 있다.

```java
public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal =
            new CustomUserDetails(customUser.name(), customUser.username());
        Authentication auth =
            new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
```

나 같은 경우는 다음처럼 작성했다.

```java
// WithMockCustomUser.java
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "martin";

    UserRole role() default UserRole.BUYER;
}

// WithMockCustomUserSecurityContextFactory.java
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserContext userContext = new UserContext(1L, "aabb", customUser.username(), customUser.role());
        PostAuthorizationToken token = new PostAuthorizationToken(userContext);

        context.setAuthentication(token);

        return context;
    }
}
```

이제 이렇게 작성한 후에는 테스트 클래스나 테스트 메서드에 우리의 새로운 어노테이션을 사용할 수 있다.

WithSecurityContextFactory의 구현체를 만들 때, 표준 스프링 어노테이션을 사용할 수 있다는 장점이 있다. 예를 들어 WithUserDetailsSecurityContextFactory에서 @Autowired를 사용할 수 있다.
