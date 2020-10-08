# Spring Security

## AuthenticationManager

AuthenticationProvider들을 담고 있는 공간이라고 생각하면 됨. 등록된 Authentication Provider들에 접근하는 유일한 객체. 내장된 구현체는 ProviderManager다.

AuthenticationManager를 살펴보면 다음과 같다.

```java
public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
```

주석을 읽고 해석 해보면, 통과된 Authentication 객체를 인증하려고 시도하고, 성공한 경우 완전히 채워진(?) Authentication 객체를 반환한다. AuthenticationManager는 예외에 대해 다음을 지켜야한다.

- DisabledException은 Account가 비활성화 되었고, AuthenticationManager가 이 상태를 테스트할 수 있는 경우 발생시켜야한다.
- LockedException은 계정이 잠겨있고, AuthenticationManager가 account가 잠겨있음을 테스트 할 수 있는 경우 발생시켜야한다.
- BadCredentialsException은 잘못된 자격 증명이 나타났을때 발생시켜야 한다. 위의 예외는 선택사항이지만, AuthenticationManager는 항상 credentials을 테스트 해야한다.

위에서 설명한 순서대로 예외를 테스트 해야한다.(예를 들어, 계정이 비활성화 또는 잠기면 인증 요청이 즉시 거부되고, 자격증명 프로세스가 수행되지 않음) 이렇게 해서 비활성화된 계정이나 잠긴 계정에 대해 자격 증명 프로세스가 진행되는 것을 막아준다.

## AuthenticationProvider

진짜 인증이 일어나는 곳, 인증 전 객체를 받아 인증 가능 여부를 체크한 후 예외를 던지거나 인증 후 객체를 만들어서 돌려준다.

필요에 맞게 구현하고 AuthenticationManager에 등록시키자.

## UserNamePasswordAuthenticationToken

UserName과 Password의 간편한 표현을 위해서 설계됬다. principle과 credential로 구성되어 있다. 그러니까 User를 구분할 구분자와 password를 가진 객체(토큰) 인 것이다.

생성자가 두개 있는데, 나는 이 생성자들을 이렇게 구현했다.

```java
// PostAuthorizationToken.java
public class PostAuthorizationToken extends UsernamePasswordAuthenticationToken {
    public PostAuthorizationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

// PreAuthorizationToken.java
public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {
    public PreAuthorizationToken(Email email, Password password) {
        super(email, passoword);
    }
}
```

그래서 PreAuthorizationToken는 인증 전 객체, PostAuthorizationToken는 인증 후 객체로 사용한다.

### Principle

인증의 주체가 되는 객체들? 그러니까 흔히 우리는 User의 ID로 사용자들을 구분하기 때문에, 여기서는 User의 ID이다. 내가 개발하고 있는 것에는 Email 객체가 사용자를 구분하는 주체이므로 principle을 Email 객체로 사용하면 된다.

### Credential

패스워드라고 생각하면 편함. Principle을 권한을 주거나 인증을 시키기 위해 확인할 자격증명. 나는 Password 객체가 Credential이다.

## Provider

인증을 처리하는 곳, 인증 전 객체(PreAuthorizationToken)을 받아 인증 완료 후 인증 후 객체(PostAuthorizationToken)으로 반환해준다. 인증 실패 시 Exception 발생

인증을 처리해야 할 객체가 분리되어야 할 필요가 있을 때마다 만든다.

## Filter

요청이 들어왔을 때 가장 앞단에 있는 것

만약에 Form login과 Social login을 구현한다고 하면 filter는 각 로그인 마다 하나씩, 총 두개 필요하다. 엔드포인트가 달라지고, 엔드포인트에 따른 흐름이 달라지기 때문에, 따로 두는 것이 좋다.

## Handler

인증의 성공/실패 시 이후 처리를 한다. 예를 들어 LoginSuccessHandler에서는 로그인이 성공했으니, 메인 페이지로 redirect를 시키는 작업을 할 수 있다.

인증 실패 시 LoginFailHandler에서는 로그인이 실패했으니, 어떤 원인으로 실패 했는지 사용자에게 알려주고 다시 로그인 할 수 있도록 로그인 페이지로 다시 보내는 작업을 할 수 있다.

## 참고자료

- [Youtube - 봄이네집](https://www.youtube.com/channel/UCQqSNFQ3TI7x0l06UUGldxQ)
