# AuthenticationManager

Filter를 거쳐 인증 전 토큰 객체를 받는다.

AuthenticationProvider 목록 중에서 조건에 맞는 AuthenticationProvider를 찾아 인증처리를 위임한다. AuthenticationManager의 구현체는 ProviderManager가 있는데, 이 코드를 살펴보면 다음과 같은 부분이 있다.

```java
for (AuthenticationProvider provider : getProviders()) {
    if (!provider.supports(toTest)) {
        continue;
    }

    if (debug) {
        logger.debug("Authentication attempt using "
                + provider.getClass().getName());
    }

    try {
        result = provider.authenticate(authentication);

        if (result != null) {
            copyDetails(authentication, result);
            break;
        }
    }
    catch (AccountStatusException | InternalAuthenticationServiceException e) {
        prepareException(e, authentication);
        // SEC-546: Avoid polling additional providers if auth failure is due to
        // invalid account status
        throw e;
    } catch (AuthenticationException e) {
        lastException = e;
    }
}
```

이 처럼 provider list 중에 조건에 맞지 않는(`!provider.supports(toTest)`) provider는 넘어가고, 조건에 맞는 provider를 찾아서 인증처리를 위임한다.(`result = provider.authenticate(authentication);`)

현재 ProviderManager의 부모 ProviderManager를 설정해서 AuthenticationProvider를 탐색할 수 있는데, 그 코드는 다음과 같다.(이 방식은 아직 사용해보지 않아서 명확하게 어떤 의미인지 모르겠다.)

```java
if (result == null && parent != null) {
    // Allow the parent to try.
    try {
        result = parentResult = parent.authenticate(authentication);
    }
    catch (ProviderNotFoundException e) {
        // ignore as we will throw below if no other exception occurred prior to
        // calling parent and the parent
        // may throw ProviderNotFound even though a provider in the child already
        // handled the request
    }
    catch (AuthenticationException e) {
        lastException = parentException = e;
    }
}
```
