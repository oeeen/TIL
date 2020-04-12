# Overloading과 Overriding

## Overloading

메서드가 동일한 이름을 가지고 있지만 return type이나 parameter가 다른 메서드

java api를 참고하면 쉽게 확인할 수 있는데, time api에서 of 메서드를 찾아보면 다음과 같이 overloading 되어있다.

![LocalDateTime.of](/2020/assets/img/LocalDateTime_of.png)

동일한 이름을 가지지만 parameter 개수나 타입이 다르다.

## Overriding

상위 클래스에서 정의한 메서드를 하위 클래스에서 재정의 하는 것. 동일한 시그니처를 가져야한다. 보통은 @Override 어노테이션을 달아줘야 헷갈리지 않는다.

Overriding도 코드를 짜면서 많이 확인할 수 있는데, 이전에 했던 프로젝트에서 예시를 가져오면 다음과 같이 있었다.

```java
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginSession = session.getAttribute("loginUser");

        if (loginSession == null) {
            response.sendRedirect("/");

            return false;
        }

        return true;
    }
}
```

HandlerInterceptorAdapter는 abstract class이고 그 보다 상위 인터페이스인 HandlerInterceptor에 preHandle 메서드는 default method로 아래처럼 되어있다.

```java
public interface HandlerInterceptor {

    /**
    * Intercept the execution of a handler. Called after HandlerMapping determined
    * an appropriate handler object, but before HandlerAdapter invokes the handler.
    * <p>DispatcherServlet processes a handler in an execution chain, consisting
    * of any number of interceptors, with the handler itself at the end.
    * With this method, each interceptor can decide to abort the execution chain,
    * typically sending a HTTP error or writing a custom response.
    * <p><strong>Note:</strong> special considerations apply for asynchronous
    * request processing. For more details see
    * {@link org.springframework.web.servlet.AsyncHandlerInterceptor}.
    * <p>The default implementation returns {@code true}.
    * @param request current HTTP request
    * @param response current HTTP response
    * @param handler chosen handler to execute, for type and/or instance evaluation
    * @return {@code true} if the execution chain should proceed with the
    * next interceptor or the handler itself. Else, DispatcherServlet assumes
    * that this interceptor has already dealt with the response itself.
    * @throws Exception in case of errors
    */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return true;
    }
```

이렇게 상위 클래스에 구현되어 있는 메서드를 하위 클래스에서 재구현하는 것이 Overriding 이다.
