# Spring Security - 인증

## Authentication

사용자의 인증 정보를 저장하는 토큰 개념, 현재 진행중인 프로젝트에서 PreAuthorizationToken, PostAuthorizationToken 과 같은 객체다.

```java
public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(Email email, Password password) {
        super(email, password);
    }
...

// UsernamePasswordAuthenticationToken.java
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
...

// AbstractAuthenticationToken.java
public abstract class AbstractAuthenticationToken implements Authentication,
        CredentialsContainer {
```

나 같은 경우에는 PreAuthorizationToken을 인증 전 토큰, PostAuthorizationToken을 인증 후 토큰으로 사용했다.

인증 후 토큰은 SecurityContextHolder 내의 SecurityContext에 담겨있어 이 Authentication 객체는 전역적으로 사용할 수 있다.

### 구조

1. principal - 사용자 아이디(사용자를 구분할 수 있는 것)
2. credential - 사용자 비밀번호
3. authorities - 인증 후 사용자의 권한 목록
4. details - 인증 부가 정보
5. Authenticated - 인증 여부

## SecurityContext

Authentication 객체가 저장되는 저장소. ThreadLocal 에 저장되어서, 동일 쓰레드 내 전역적으로 참조가 가능하게 설계됨. 인증이 완료되면 HttpSession 에 저장된다.

```java
public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
    /**
     * The default key under which the security context will be stored in the session.
     */
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
```

`SPRING_SECURITY_CONTEXT`라는 키로 HttpSession에서 접근 할 수 있다. 위 클래스를 살펴보면 어떤 방식으로 Session에 SecurityContext를 저장하는지 알 수 있다.

## SecurityContextPersistenceFilter

1. SecurityContext 객체 생성, 저장, 조회
2. 최종 응답 후 SecurityContextHolder.clearContext() 한다.

### 인증 시

인증 시 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장한다. Filter를 통해 인증 성공 후 SecurityContext에 Authentication 객체를 저장한다. 인증이 최종 완료되면 HttpSession에 SecurityContext를 저장한다.

### 인증 후

1. Session에서 SecurityContext를 꺼내서 SecurityContextHolder 에 저장
2. SecurityContext 내에 Authentication 객체가 존재하면 인증된 상태를 유지한다.

### 동작 흐름

1. 요청이 SecurityContextPersistenceFilter로 들어온다.
2. HttpSecurityContextRepository에서 인증이 되었는지 확인한다.
3. 인증이 안된 상태라면 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장한다.
    1. FilterChain을 돌면서 인증 절차를 거친 후 인증 성공 후 SecurityContext에 Authentication 객체를 저장한다.
    2. 인증 최종 완료 후 HttpSession에 SecurityContext를 저장한다.
    3. SecurityContextHolder에서 SecurityContext를 clear한다.
4. 인증이 된 상태라면 Session에서 SecurityContext를 꺼내서 SecurityContextHolder에 저장한다.
    1. SecurityContext 내에 Authentication 객체가 있는지 확인 한 후 있다면, 인증된 상태를 유지한다.

## Spring Security 인증 흐름

Spring Security에서 인증 흐름을 내 프로젝트에서 작성한 코드 기반으로 살펴본다.

1. 사용자의 Form login 요청이 들어온다(Email, Password)
2. `public class FormLoginFilter extends AbstractAuthenticationProcessingFilter`에서 인증 전 토큰 객체를 생성한다.
3. AuthenticationManager에 넘긴다.
4. AuthenticationManager는 직접 검증을 하는 것이 아니라 AuthenticationProvider에 위임한다.
5. `public class FormLoginAuthenticationProvider implements AuthenticationProvider`에서 실제 인증 절차를 거친다.
6. FormLoginAuthenticationProvider에서 인증이 완료되면, 인증 후 객체를 AuthenticationManager에 넘긴다.
    - 인증 실패 시 UnauthorizedException을 던진다.
7. AuthenticationManager는 FormLoginFilter로 Authentication 객체를 넘기고, 이 Authentication 객체는 SecurityContext에 저장된다.

FormLoginFilter에서 인증 전 토큰 객체를 AuthenticationManager에 넘기는 부분 코드

```java
 @Override
public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    UserLoginRequest userLoginRequest = new UserLoginRequest(new Email(email), new Password(password));
    PreAuthorizationToken token = new PreAuthorizationToken(userLoginRequest);

    logger.debug("Requested Login Email: {}", email);

    return super.getAuthenticationManager().authenticate(token);
}
```

FormLoginAuthenticationProvider에서 인증 과정을 수행하는 부분 코드

```java
// FormLoginAuthenticationProvider.java
@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    PreAuthorizationToken token = (PreAuthorizationToken) authentication;

    Email email = token.getEmail();
    Password password = token.getUserPassword();

    User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException(email.getEmail()));

    if (userService.isCorrectPassword(password, user)) {
        return new PostAuthorizationToken(userService.findByEmail(email));
    }
    throw new UnauthorizedException();
}
```

실제 프로젝트 코드는 [https://github.com/oeeen/commerce](https://github.com/oeeen/commerce) 에 있음
