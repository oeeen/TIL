# SessionManagementFilter

1. 세션관리
2. 동시적 세션 제어
3. 세션 고정 보호
4. 세션 정책 생성

## ConcurrentSessionFilter

**매 요청**마다 현재 사용자의 세션 만료 여부 체크, 세션이 만료되었을 경우 즉시 만료 처리

```java
if (session.isExpired()) {
    로그아웃
    오류 페이지 응답
}
```

## 동시적 세션 제어

1. 사용자 A는 로그인 한 상태다. 최대 세션 허용 개수는 1개라고 가정
2. SessionManagementFilter로 새로운 로그인(B)이 들어온다.
3. 기존 세션은 만료된다.(session.expireNow())
4. 지금 세션 정책은 이전 사용자 세션을 만료시키는 정책이다.
5. ConcurrentSessionFilter에 이전 사용자(A)의 요청이 들어온다.
6. 세션이 만료되었는지 확인하는 작업을 SessionManagementFilter를 참조한다.
7. ConcurrentSessionFilter에서 Logout, 오류 페이지를 응답한다.

## 세션 관련 전반적인 처리 과정

최대 허용 세션 개수가 1개, 세션 정책은 이전 사용자 세션을 만료시키는 정책, 세션 고정 보호 정책이라고 가정

1. 사용자 A가 로그인 요청
2. UsernamePasswordAuthenticationFilter에서 인증 처리
3. ConcurrentSessionControlAuthenticationStrategy 호출, 세션 카운트를 확인한다.(session count = 0)
4. ChangeSessionIdAuthenticationStrategy 에서 세션 고정 보호 (session.changeSessionId())
5. RegisterSessionAuthenticationStrategy에서 사용자의 세션 등록(사용자 A, session count 1)
6. 사용자 B가 로그인 요청
7. UsernamePasswordAuthenticationFilter에서 인증 처리
8. ConcurrentSessionControlAuthenticationStrategy 호출, 세션 카운트를 확인한다.(session count = 1)
    - 인증 실패 전략인 경우: SessionAuthenticationException 발생, 인증 실패
    - 세션 만료 전략인 경우: 사용자 A의 세션을 만료시킴(session.expireNow() - 사용자 A)
9. ChangeSessionIdAuthenticationStrategy 에서 세션 고정 보호 (session.changeSessionId())
10. RegisterSessionAuthenticationStrategy에서 사용자의 세션 등록(사용자 B, session count 1)
11. 만약 사용자 A가 권한이 필요한 요청을 하면, ConcurrentSessionFilter가 세션 만료를 확인한다.(session.isExpired() == true, ConcurrentSessionControlAuthenticationStrategy에서 session.expireNow() 확인)
12. ConcurrentSessionFilter에서 사용자 Logout, 오류 페이지 응답
