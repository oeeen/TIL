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