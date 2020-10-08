# Spring Security 에서 로그아웃을 구현하는 방법

## RequestMapping으로 Controller 단에서 처리하는 법

```html
<li sec:authorize="isAuthenticated()" class="nav-item">
    <a class="nav-link" href="/logout">로그아웃</a>
</li>
```

원래 위 코드처럼 a tag로 logout 요청을 보내도록 구현되어 있었다.(현재는 Post방식으로 처리하기 위해 js쪽에서 처리한다.)

위 방식을 유지하면서(html에서 a tag로 logout 요청을 보내고(GET)), 이를 간단하게 Controller 단에서 처리하려면 Controller에 다음처럼 코드를 작성하면 된다.

```java
@GetMapping("/logout")
public RedirectView logout(HttpServletRequest req, HttpServletResponse res) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
        new SecurityContextLogoutHandler().logout(req, res, authentication);
    }

    return new RedirectView("/");
}
```

당연하겠지만 PostMapping으로 바꾸고 logout 요청을 처리하는 것을 Security와 Controller에 모두 작성한다면, Controller에는 걸리지 않는다. Controller보다 LogoutFilter가 먼저 걸리기 때문에, 여기서 LogoutSuccessHandler까지 모두 타고 처리된다.

## Security Configuration에서 처리하는 법

나는 이 방식으로 구현했다. configuration 클래스의 configure 메서드를 다음처럼 작성했다.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/api/users").hasRole(UserRole.ADMIN.name())
            .antMatchers("/", "/api/users/signup", "/login/**", "/signup").permitAll()
            .antMatchers("/api/products/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/api/users/signin")
            .defaultSuccessUrl("/")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/api/users/logout")
            .logoutSuccessUrl("/")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .logoutSuccessHandler(new CustomLogoutSuccessHandler());
}
```

### 로그아웃 과정

1. 요청한 URL을 LogoutFilter에서 맞는 지 확인 후(`logoutRequestMatcher.matches(request)`)
2. SecurityContext에서 Authentication 객체를 가져온다.
3. LogoutHandler들(`CsrfLogoutFilter`, `SecurityContextLogoutHandler`, `LogoutSuccessEventPublishingLogoutHandler`)에서 logout 작업을 수행한다.
    - `CsrfLogoutFilter`에서는 `this.csrfTokenRepository.saveToken(null, request, response);` 한다.
    - `SecurityContextLogoutHandler`에서는 `invalidateHttpSession`, `clearAuthentication` 값에 따라 행동한다. (true면 무효화, clear 등 예상되는 행동)
    - `LogoutSuccessEventPublishingLogoutHandler`에서는 `eventPublisher.publishEvent(new LogoutSuccessEvent(authentication));`하는데 아직 EventPublisher가 무엇을 하는지 모르겠다.
4. LogoutSuccessHandler에서 로그아웃 후 작업을 수행한다.

### 각 API에 대해 하나하나

1. `clearAuthentication(true)`는 인증 객체를 클리어 하는 것이고, default값이 true인데 명시적으로 적어주었다.
2. `invalidateHttpSession(true)`도 HttpSession을 invalidate 하는 것이고, default 값이 true지만 명시적으로 적어주었다.
3. `logoutUrl(url)`은 default로 CSRF 보호가 적용되어 있다면 POST방식으로만 logout url로 동작시킬 수 있고, CSRF가 꺼져있다면 다른 어떤 Http method도 사용할 수 있다.
    - 그러나 웬만하면 상태를 변경하는 행동은 POST를 사용하는 것이 CSRF 공격으로부터 보호하는데 좋다. 그래도 로그아웃에 GET 메서드를 사용하고 싶다면, `logoutRequestMatcher(new AntPathRequestMatcher(logoutUrl, "GET")`를 사용하면 된다.
4. `logoutSuccessUrl("/")`
    - 로그아웃이 일어난 이후 redirect 될 url을 지정한다. default는 `/login?logout` 이다.
5. `logoutSuccessHandler(new CustomLogoutSuccessHandler())`
    - 사용할 logoutSuccessHandler를 지정한다. 이 값을 설정하면 logoutSuccessUrl을 설정한 것은 무시된다. 그래서 구현한 CustomLogoutSuccessHandler에서 redirect를 해주어야 할 것 같다.
