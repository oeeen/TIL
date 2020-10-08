# Database initialization

SQL database는 사용하는 데이터베이스에 따라 다른 방법으로 초기화 할 수 있다. 아니면 수동으로 초기화 할 수도 있다.

## JPA를 이용한 데이터베이스 초기화

JPA는 DDL 생성을 위한 기능을 가지고 있고, 데이터베이스가 시작할 때 실행되도록 설정할 수 있다. 이는 다음의 두 가지 외부 설정값에 의해 제어된다.

1. spring.jpa.generate-ddl(boolean)
    - 기능을 켜거나 끈다.(true/false)
    - 벤더에 독립적이다.
2. spring.jpa.hibernate.ddl-auto(enum)
    - none, validate, update, create-drop

## Hibernate를 이용한 데이터베이스 초기화

spring.jpa.hibernate.ddl-auto를 명시적으로 설정할 수 있고, 표준 hibernate 속성 값은 none, validate, update, create-drop이 있다. Spring Boot는 데이터베이스가 내장되어 있다면 create-drop, 아니라면 none을 기본값으로 가진다. 내장 데이터베이스는 connection type이 hsqldb, h2, derby이면 내장이라고 감지하고, 그 외라면 내장이 아니라고 감지한다. 인-메모리 데이터베이스로부터 새 플랫폼의 데이터와 테이블의 존재에 대한 추측이 없는 실제 데이터베이스로 변경할 때는 주의해라. ddl-auto를 명시적으로 설정하거나, 다른 메커니즘 중 하나를 선택하여 데이터베이스를 초기화 해야한다.

또한 classpath에 `import.sql`이라는 파일은 데이터베이스 시작 시 실행 된다. 이것은 조심히 사용한다면 테스트나 데모할 때는 유용할 수 있지만, 프로덕션 단계에서는 아마 클래스패스에 있는 것을 사용하고 싶지는 않을 것이다. 이는 hibernate의 기능이다.(Spring과 무관)

## Spring JDBC를 이용한 데이터베이스 초기화

Spring JDBC는 DataSource 초기화 기능이 있다. Spring Boot는 이를 기본적으로 활성화하고, 표준 위치의 `schema.sql`과 `data.sql`으로부터 SQL을 로드한다.(classpath root) Spring Boot는 `schema-${platform}.sql`과 `data-${platform}.sql` 파일을 로드 할 것이다.(platform은 spring.datasource.platform) 예를 들어, 사용하는 데이터베이스의 벤더 이름(hsqldb, h2, oracle, mysql...)을 아마 고를 것이다. Spring Boot는 JDBC initializer의 failfast 기능을 활성화 시켜놓아서, 스크립트에 예외가 발생하면 어플리케이션이 시작하는데 실패할 것이다. 이 스크립트 위치는 spring.datasource.schema와 spring.datasource.data의 설정을 바꿈으로써 변경할 수 있다. spring.datasource.initialize=false라도 초기화가 진행될 것이다.

failfast를 비활성화 하기 위해서는 `spring.datasource.continueOnError=true`로 설정하면 된다. 이는 어플리케이션이 몇 번 배포 되었고, 좀 성숙했다면 유용할 것이다. 실패하면, 데이터가 이미 존재한다는 것을 의미하므로 어플리케이션의 실행을 막을 필요가 없다는 뜻이다.

만약 JPA 어플리케이션(hibernate와 함께)의 초기화에 `schema.sql`을 사용하기를 원한다면 `ddl-auto=create-drop`은 hibernate가 같은 테이블을 만들기 때문에 에러가 발생할 것이다. 이런 에러를 피하기 위해 ddl-auto를 명시적으로 `""` 또는 `none`으로 설정해야한다. `ddl-auto=create-drop`을 하던 말던 항상 `data.sql`을 새로운 데이터를 초기화하는데 사용할 수 있다.

## 참고자료

- [스프링 공식문서](https://docs.spring.io/autorepo/docs/spring-boot/1.2.0.M2/reference/html/howto-database-initialization.html)
