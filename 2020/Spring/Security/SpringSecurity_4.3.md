# Spring Security Reference 읽기

## 4.3 Gradle

### 4.3.1 Spring Boot with Gradle

스프링 부트는 spring-boot-starter-security라는 스프링 시큐리티 관련 종속성을 함께 모으는 스타터를 제공한다. 가장 간단하고 선호되는 방법은 spring initializer를 사용하는 것이다.(IDE나 `https://start.spring.io`를 통한)

아니면 아래와 같이 수동으로 starter를 추가할 수도 있다. (reference에는 compile로 되어있어서 implementation으로 변경)

```groovy
dependencies {
    implementation "org.springframework.boot:spring-boot-starter-security"
}
```

스프링 부트는 종속성 관리를 위해 메이븐 BOM을 제공하기 때문에, 특정한 버전을 지정할 필요가 없다. 만약 스프링 시큐리티 버전을 재정의 하려고 한다면, 다음과 같이 Gradle property에 적으면 된다.

```groovy
ext['spring-security.version']='5.3.2.BUILD-SNAPSHOT'
```

스프링 시큐리티는 메이저 릴리즈에서만 주요 변경점을 적용하기 때문에, 스프링 부트와 함께 스프링 시큐리티의 최신 버전을 사용하는 것이 안전하다. 그러나 가끔 Spring Framework의 버전도 업데이트 해야 할 수도 있다. 아래 처럼 변경하면 된다.

```groovy
ext['spring.version']='5.2.7.BUILD-SNAPSHOT'
```

만약 추가적인 기능(LDAP, OpenID 등)을 사용한다면, 적합한 프로젝트 모듈을 포함 할 필요가 있다.

### 4.3.2. Gradle Without Spring Boot

스프링 부트 없이 스프링 시큐리티를 사용할 때 선호되는 방법은 스프링 시큐리티의 BOM을 사용하여 전체 프로젝트를 통틀어 일관된 버전의 스프링 시큐리티를 사용하는 것이다. 다음 예시처럼 Dependency Management Plugin을 사용하여 이를 수행할 수 있다.

```groovy
plugins {
    id "io.spring.dependency-management" version "1.0.6.RELEASE"
}

dependencyManagement {
    imports {
        mavenBom 'org.springframework.security:spring-security-bom:5.3.2.BUILD-SNAPSHOT'
    }
}
```

스프링 시큐리티 Maven의 최소 의존성 세트는 다음과 같다.

```groovy
dependencies {
    implementation "org.springframework.security:spring-security-web"
    implementation "org.springframework.security:spring-security-config"
}
```

만약 추가적인 기능(LDAP, OpenID 등)을 사용한다면, 적합한 프로젝트 모듈을 포함 할 필요가 있다.

스프링 시큐리티는 Spring Framework 5.2.7.BUILD-SNAPSHOT에 대해 빌드된다. 그러나 일반적으로 Spring Framework 5.x 이후 버전에서는 모두 동작한다.

### 4.3.3. Gradle Repositories

모든 GA 릴리즈(.RELEASE로 끝나는 버전)는 Maven Central에 배포되므로 `mavenCentral()`을 사용하면 GA release를 사용하는데 충분하다. 다음 예시는 이렇게 하는 방법을 보여준다.

```groovy
repositories {
    mavenCentral()
}
```

만약 SNAPSHOT 버전을 사용하고 싶다면, 다음과 같이 Spring snapshot repository를 정의하고 사용하면 된다.

```groovy
repositories {
    maven { url 'https://repo.spring.io/snapshot' }
}
```

마일스톤이나 릴리즈 후보 버전을 사용한다면, 아래처럼 스프링 마일스톤 레포지토리가 정의되어 있는지 확인하고 사용하면 된다.

```groovy
repositories {
    maven { url 'https://repo.spring.io/milestone' }
}
```
