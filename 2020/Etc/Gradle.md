# Gradle

> Gradle은 Groovy를 이용한 빌드 자동화 시스템이다. Groovy와 유사한 도메인 언어를 채용하였으며, 현재 안드로이드 앱을 만드는데 필요한 안드로이드 스튜디오의 공식 빌드 시스템이기도 하다. Java, C/C++, 파이썬 등과 같은 여러 가지 언어를 지원한다. (위키백과)

`apply plugin: 'java'`

```shell
> gradle build
:compileJava
:processResources
:classes
:jar
:assemble
:compileTestJava
:processTestResources
:testClasses
:test
:check
:build

BUILD SUCCESSFUL
```

## API and implementation separation

표준 Java 플러그인과 Java Library 플러그인의 주요 차이점은 후자가 소비자에게 노출되는 API의 개념을 도입한다는 것이다. 라이브러리는 다른 구성요소에 의해 사용된다. 멀티프로젝트 빌드에서는 매우 흔한 사용 사례지만, 외부 의존성이 생기자마자 바로 사용할 수 있다.

gradle에서 java plugin에서는 api와 implementation을 사용할 수 있다. api는 라이브러리 API에서 외부의 종속성을 선언하는 데 사용되어야 하는 반면, implementation은 컴포넌트 내부에 있는 종속성을 선언하는 데 사용되어야 한다.(?)

Example 2. Declaring API and implementation dependencies

```groovy
dependencies {
    api 'org.apache.httpcomponents:httpclient:4.5.7'
    implementation 'org.apache.commons:commons-lang3:3.5'
}
```

api를 사용하면 라이브러리 사용자의 컴파일 classpath에 나타난다. 한편, implementation으로 선언된 dependency는 사용자에 노출되지 않고, 따라서 사용자의 compile classpath로 유출되지 않을 것이다. 여기에는 몇 가지 이점이 있다.

- 의존성이 사용자의 compile classpath에 노출되지 않기 때문에, 결코 우연히 외부 의존성에 의존하지 않을 것
- 클래스패스 크기 감소로 컴파일 시간 감소
- implementation 종속성 변경 시 재 컴파일 감소
- 깔끔한 배포?: 새로운 maven-publish 플러그인과 함께 사용될 때 자바 라이브러리는 런타임에 사용할 라이브러리와 컴파일타임에 사용할 라이브러리를 명확하게 구분하는 POM file을 만든다.

> compile은 그대로 사용 가능하지만 api와 implementation이 제공하는 기능을 전부 제공한다고 보장할 수 없다.

## Recognizing API and implementation dependencies

가능하면 api보다 implementation 선호

사용자의 compile classpath에서 의존성을 배제한다. 또한, 어떤 implementation type이 실수로 공개 API로 유출될 경우 사용자는 즉시 컴파일하지 못할 것이다.(?)

그러면 언제 api를 써야할까? API 의존성은 라이브러리 이진 인터페이스(Library binary interface)에 노출되는 한 가지 이상의 타입을 포함하는 것으로, 흔히 ABI(Application Binary Interface)라고 한다. 다음이 포함된다.

- 슈퍼클래스 또는 인터페이스에 사용되는 유형
- public method parameter에 사용되는 유형(컴파일러에 보이는 것, 즉 public, protected, package private..)
- public fields에 사용되는 유형
- public annotation 유형

대조적으로 아래 리스트는 ABI와 무관하므로 implementation으로 선언되어야 한다.

- 메서드 바디안에서만 사용되는 유형
- private member만 사용하는 유형
- 내부 클래스에서만 사용되는 유형(gradle의 미래 버전에서는 public API에 속할 패키지를 선언할 수 있을 거다..)

### 예시 (API와 implementation의 차이)

```java
package dev.smjeon.til.gradle;

// The following types can appear anywhere in the code
// but say nothing about API or implementation usage
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpClientWrapper {

    private final HttpClient client; // private member: implementation details

    // HttpClient is used as a parameter of a public method
    // so "leaks" into the public API of this component
    public HttpClientWrapper(HttpClient client) {
        this.client = client;
    }

    // public methods belongs to your API
    public byte[] doRawGet(String url) {
        HttpGet request = new HttpGet(url);
        try {
            HttpEntity entity = doGet(request);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            entity.writeTo(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            ExceptionUtils.rethrow(e); // this dependency is internal only
        } finally {
            request.releaseConnection();
        }
        return null;
    }

    // HttpGet and HttpEntity are used in a private method, so they don't belong to the API
    private HttpEntity doGet(HttpGet get) throws Exception {
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + response.getStatusLine());
        }
        return response.getEntity();
    }
}
```

HttpClientWrapper의 public 생성자는 HttpClient를 파라미터로 사용한다. 그래서 사용자에게 노출되고, 해당 API에 의존한다. HttpGet과 HttpEntity가 private 메서드의 시그니쳐가 사용된다.(그러나 private method의 시그니쳐에 사용되었으니까 HttpClient를 API 종속성으로 만드는데는 카운트 되지 않는다.)

ExceptionUtils 유형을 보면, commons-lang 라이브러리는 메서드 바디에서만(메서드 시그니쳐에는 없고) 사용되므로 implementation으로 사용한다.(`ExceptionUtils.rethrow(e)` 이런 거)

그래서 httpclient는 api 의존성이고, 반면 commons-lang은 implementation 의존성이다. 그렇기 때문에 다음과 같이 build.gradle을 선언한다.

```groovy
dependencies {
    api 'org.apache.httpcomponents:httpclient:4.5.7'
    implementation 'org.apache.commons:commons-lang3:3.5'
}
```

## 참고자료

- [https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation)
- [https://docs.gradle.org/current/userguide/java_plugin.html](https://docs.gradle.org/current/userguide/java_plugin.html)
- [https://docs.gradle.org/current/dsl/](https://docs.gradle.org/current/dsl/)
