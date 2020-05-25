# Login Form 인증 과정

1. UserNamePasswordAuthenticationFilter
2. AntPathRequestMatcher("/login") - 요청 정보가 매칭되는지 확인
    - No: chain.doFilter
    - Yes: 3으로 이동
3. Authentication (Username + Password)을 가지고 4에 인증 요청
4. AuthenticationManager는 5에 인증 위임
5. AuthenticationProvider - 실제 인증 작업이 이뤄지는 곳
    - 인증 실패 - AuthenticationException
    - 인증 성공 - 인증 성공한 Authentication 객체를 AuthenticationManager에 반환, 6으로 이동
6. SecurtyContext에 인증 성공한 객체를 저장
7. SuccessHandler에서 성공 이후 흐름 처리

`SecurityContextHolder.getContext().getAuthentication();`를 통해 인증 성공한 Authentication 객체를 어디서든 얻을 수 있다. 이를 활용해서 이것 저것 작업을 할 수 있다.
