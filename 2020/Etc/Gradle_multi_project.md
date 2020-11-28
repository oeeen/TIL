# Gradle multi project

```groovy
Project layout
.
├── app
│   ...
│   └── build.gradle
└── settings.gradle
```

```gradle
settings.gradle

rootProject.name = 'basic-multiproject'
include 'app'
```

이런 식으로 작성한다.

Project간 dependency가 필요한 경우에도 방법이 있다.

```gradle
.
├── buildSrc
│   ...
├── api
│   ├── src
│   │   └──...
│   └── build.gradle
├── services
│   └── person-service
│       ├── src
│       │   └──...
│       └── build.gradle
├── shared
│   ├── src
│   │   └──...
│   └── build.gradle
└── settings.gradle
```

위 구조를 보면 shared, api, person-service 프로젝트가 있다. 만약 person-service가 다른 두 프로젝트에 대해 의존성을 갖고 있고, api는 shared에 대해 의존성을 갖고 있다고 해보자. 단순히 `:`를 project path에 정의하면된다.

```groovy
// settings.gradle
rootProject.name = 'dependencies-java'
include 'api', 'shared', 'services:person-service'

// buildSrc/src/main/groovy/myproject.java-conventions.gradle
plugins {
    id 'java'
}

group = 'com.example'
version = '1.0'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation "junit:junit:4.13"
}

// api/build.gradle
plugins {
    id 'myproject.java-conventions'
}

dependencies {
    implementation project(':shared')
}

// shared/build.gradle
plugins {
    id 'myproject.java-conventions'
}

// services/person-service/build.gradle
plugins {
    id 'myproject.java-conventions'
}

dependencies {
    implementation project(':shared')
    implementation project(':api')
}
```

person-service project의 build.gradle을 보면 `implementation project(':shared')` 처럼 의존성이 있는 프로젝트를 선언해줬다.

맞게 선언했는지는 모르겠지만 나는 연습 프로젝트에서 아래 처럼 build.gradle을 만들었다. 내 프로젝트 트리는 아래와 같다.

```gradle
.
├── api
│   ├── src
│   │   └──...
│   └── build.gradle
├── batch
│   ├── src
│   │   └──...
│   └── build.gradle
└── settings.gradle
```

```groovy
plugins {
    id 'org.springframework.boot' version '2.4.0'
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

repositories {
    mavenCentral()
}

subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'

    group = 'dev.smjeon'
    version = '1.0-SNAPSHOT'
    sourceCompatibility = '1.8'

    repositories {
        mavenCentral()
    }

    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }

    dependencies {
        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation('org.springframework.boot:spring-boot-starter-test') {
            exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
        }
    }

    test {
        useJUnitPlatform()
    }
}

project(':api') {
    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-web'
    }
}

project(':batch') {
    dependencies {
        implementation 'org.springframework.batch:spring-batch-core'
    }
}
```

예를 들어 공통 모듈을 모아놓은 common이라는 모듈이 있다면, 해당 프로젝트에는 main 메서드가 없을 수 있다. 그럴 경우에는 `bootJar.enabled = false, jar.enabled = true` 해야한다.

```groovy
bootJar.enabled = false
jar.enabled = true
```

사실 Intellij에서 File -> New -> Module 을 눌러서 쉽게 Module을 추가할 수 있다. 이렇게 추가한 이후에 추가로 더 필요한 작업은 수동으로 해주면 된다.

![Intellij new module 1](/2020/assets/img/intellij-module1.png)

![Intellij new module 2](/2020/assets/img/intellij-module2.png)

Intellij로 새로운 모듈을 추가하면, 최상단의 gradle.settings에 해당 모듈이 rootProject 밑으로 include 되는 작업까지만 된다. 그래서 추가 된 프로젝트에 새로운 의존성이 필요하면 그 프로젝트의 build.gradle에 의존성을 추가해주면 된다. 또한 새로 생성한 프로젝트가 다른 프로젝트에 의존성이 있다면 위에서 말한 `:`를 이용해서 의존성을 추가해주면 된다.

## 참고자료

- [Gradle docs - Multi-project Builds in gradle](https://docs.gradle.org/current/userguide/multi_project_builds.html#sec:creating_multi_project_builds)
