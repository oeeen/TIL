# 인가 API

## 권한 설정

1. URL
    - http.antMatchers("/users/**").hasRole("ROLE_USER")
2. Method
    - @PreAuthorize("hasRole('ROLE_USER')") - 메소드 위에

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .antMatcher("/users")
            .authorizeRequests()
            .antMatchers("/users/login", "/users/**").permitAll()
            .antMatchers("/users/list").hasRole("ROLE_ADMIN")
            .antMatchers("/users/mypage").access("hasRole('ROLE_USER'))")
            .anyRequest().authenticated();
}
```

*설정 시 구체적인 경로가 먼저 오고, 그것보다 큰 범위의 경로는 뒤에 오도록 해야한다. Security는 인가처리를 할 때 위에서부터 순서대로 인가처리를 하기 때문에, 앞에 더 큰 범위가 오게되면 큰 URL에 권한이 허용되기 때문에, 의도한대로 동작하지 않을 수 있다.

일반적으로 ADMIN 권한이 USER보다 권한이 높은 경우가 있기 때문에 권한 계층을 설정 해주어야 한다. 또한 실제로 운영하는 서버에서 자원에 대한 접근을 코드를 작성해야 한다면 불편할 수 있다.(선언적방식) 그렇기 때문에 동적 방식으로 매핑하는 방식을 해보도록 하자.(다음에 알아보자)
