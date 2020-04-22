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

## 참고자료

- [https://docs.gradle.org/current/dsl/](https://docs.gradle.org/current/dsl/)
