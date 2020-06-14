# CSRF (Cross-site request forgery)

> 사이트 간 요청 위조(또는 크로스 사이트 요청 위조, 영어: Cross-site request forgery, CSRF, XSRF)는 웹사이트 취약점 공격의 하나로, 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위(수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 하는 공격을 말한다. (위키백과)

공격의 흐름은 다음과 같다.

1. 사용자는 웹사이트에 접속하여 정상적으로 쿠키를 발급받는다.
2. 공격자는 사용자에게 어떤 링크를 보낸다.
    - 예를 들어 `http://www.attack.com/attack`
3. 해당 링크의 html은 어떤 img tag를 포함한다.
    - 해당 img tag는 `<img src= "https://travel.service.com/travel_update?.src=Korea&.dst=Hell">` 이런 식으로 되어있다.
4. 사용자가 2번의 링크를 클릭해서 브라우저에서는 html을 읽고 뿌려주기 위해서 img tag의 링크를 연다.
5. 사용자의 쿠키를 가지고 접속했기 때문에, 공격자가 의도한 대로 동작하게 된다.

## 방어 방법

### Referer 검증

Back 단에서 request의 referer를 확인하여 도메인이 일치하는지 확인한다. 같은 도메인에서 요청이 들어오는 지 확인하고 방어하는 것. 보통 다른 피싱 사이트나, 이메일을 통해 요청이 들어오기 때문에 이 방법으로도 많이 방어할 수 있다.

### CSRF 토큰

CSRF 방어가 필요할 때마다 CSRF 토큰을 발행한다. 랜덤하게 생성된 CSRF 토큰을 HTTP 파라미터로 요청하고, 서버에 저장된 토큰 값과 일치하지 않으면 요청을 거부한다.

- Client: `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />`
- Spring Security: `http.csrf()`

위에 나온 공격 흐름에서 4번에서 CSRF 토큰이 없거나 일치하지 않기 때문에 공격자가 의도한 대로 상태를 변경하거나(로그아웃, 회원정보 변경 등) 할 수 없다.

SpringSecurity에서는 다음과 같은 흐름으로 동작한다.

1. CsrfFilter에서 doFilter() 동작
2. tokenRepository에서 Token load
3. 기존에 발급된 토큰이 없으면 토큰을 새로 발급하고 tokenRepository에 저장
4. `request.setAttribute`로 csrfToken값 저장
5. request header에서 실제 토큰을 가져온다. (client에서 보낸)
6. `!csrfToken.getToken().equals(actualToken)` 이면 AccessDenied, 토큰이 일치하면 CSRF 검사 통과하고 문제없이 다음 필터로 넘어감

```java
// CsrfFilter.java
@Override
protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
    request.setAttribute(HttpServletResponse.class.getName(), response);

    CsrfToken csrfToken = this.tokenRepository.loadToken(request); // 2번 동작
    final boolean missingToken = csrfToken == null;
    if (missingToken) { // 3번 동작
        csrfToken = this.tokenRepository.generateToken(request);
        this.tokenRepository.saveToken(csrfToken, request, response);
    }
    request.setAttribute(CsrfToken.class.getName(), csrfToken);
    request.setAttribute(csrfToken.getParameterName(), csrfToken); // 4번 동작

    if (!this.requireCsrfProtectionMatcher.matches(request)) { // Csrf 보호 대상인지 확인
        filterChain.doFilter(request, response);
        return;
    }

    String actualToken = request.getHeader(csrfToken.getHeaderName()); // 5번 동작
    if (actualToken == null) {
        actualToken = request.getParameter(csrfToken.getParameterName()); // header말고 parameter로 들어왔는지 확인
    }
    if (!csrfToken.getToken().equals(actualToken)) { // 6번 동작, 서버에 저장된 토큰과 일치하는지 확인
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Invalid CSRF token found for "
                    + UrlUtils.buildFullRequestUrl(request));
        }
        if (missingToken) { // 들어온 요청에 아예 Csrf 토큰이 없는 경우
            this.accessDeniedHandler.handle(request, response,
                    new MissingCsrfTokenException(actualToken));
        }
        else { // 들어온 요청에 Csrf 토큰은 있지만, 서버에 저장된 토큰과 다른 경우
            this.accessDeniedHandler.handle(request, response,
                    new InvalidCsrfTokenException(csrfToken, actualToken));
        }
        return;
    }

    filterChain.doFilter(request, response);
}
```
