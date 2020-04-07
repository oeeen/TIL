# AOP, DI

## AOP (Aspect Oriented Programming)

관점 지향 프로그래밍이라고 한다. 관점 지향은 쉽게 말해 어떤 로직을 기준으로 핵심적인 관점, 부가적인 관점으로 나누어서 보고 그 관점을 기준으로 각각 모듈화하겠다는 것이다. 여기서 모듈화란 어떤 공통된 로직이나 기능을 하나의 단위로 묶는 것을 말한다.

```java
class A {
    method a() {
        AAAA
        치킨을 먹는다.
        BBBB
    }

    method b() {
        AAAA
        삼겹살을 먹는다.
        BBBB
    }
}
class B {
    method c() {
        AAAA
        공부를 하고, 면접을 본다.
        BBBB
    }
}
```

위에서 AAAA, BBBB라는 중복 로직을 제거할 수 있다.

```java
class AAAABBBB {
    method aaaabbbb(JoinPoint point) {
        AAAA
        point.execute()
        BBBB
    }
}
```

보통은 로깅같은 과정을 AOP로 빼서 모든 컨트롤러를 타기전에 동작하도록 작성할 수 있다. 그리고 트랜잭션 처리를 AOP를 통해 비즈니스 로직과 분리한다. 트랜잭션 처리는 데이터베이스와 관련된 내용이기 때문에, InfraStructure와 깊은 연관을 가지고 있다. 이 인프라 레벨과 비즈니스 로직과의 연관을 끊기 위해 AOP를 통해 분리한다.

스프링의 AOP는 프록시를 통해 AOP를 구현하고 있다.(JDK dynamic proxy, CGLib proxy)

## DI (Dependency Injection)

- 의존 객체 주입으로 변화에 유연하게 대응할 수 있게 만든다.
- A객체가 B객체를 의존하고 있을 때 결합도를 낮출 수 있는 가장 좋은 방법이다.
- 예시를 들어보면, SpellChecker

의존성 주입은 객체 간의 결합을 약하게 해주고 유지보수가 좋은 코드를 만들어 준다.

DI는 클래스 사이의 의존관계를 빈 설정 정보를 바탕으로 컨테이너가 자동적으로 연결해주는 것을 말한다. 개발자들은 제어를 담당할 필요없이 빈(bean) 설정 파일에 의존관계가 필요하다는 정보만 추가해주면 된다. 컨테이너가 실행 흐름의 주체가 되어 애플리케이션 코드에 의존관계를 주입해주는 것.

### IoC Container

IoC (Inversion Of Control)

일반적으로 어플리케이션을 작성하면, 이 어플리케이션의 흐름을 제어하는 것은 어플리케이션 코드이다. 그러나 Spring 기반의 개발에서는 프레임워크가 흐름을 제어하는 주체가 된다.

스프링에서 빈들의 생성 부터 생명주기 까지 책임지고 관리한다. 그렇게 함으로써 개발자는 도메인의 핵심 비즈니스 로직에 더 집중할 수 있게 된다.

IoC Container - Bean을 만들고 엮어주며 제공해준다.(이름/ID, Type, Scope)

#### Bean

스프링 IoC Container가 관리하는 객체

어떻게 등록하지? - Component Scan(@Component, @Repository, @Service, @Controller …)

어떻게 꺼내쓰지? - @Autowired(생성자 주입을 사용할 때 생성자가 하나인 경우 Autowired를 생략할 수도 있다.), @Inject, ApplicationContext에서 getBean을 통해 직접 꺼낼 수도 있다.

## Spring Core

- Servlet Container

서블릿 컨테이너는 개발자가 웹서버와 통신하기 위하여 소켓을 생성하고, 특정 포트에 리스닝하고, 스트림을 생성하는 등의 복잡한 일들을 할 필요가 없게 해준다. 컨테이너는 servlet의 생성부터 소멸까지의 일련의 과정(Life Cycle)을 관리한다. 대표적인 Servlet Container가 Tomcat 이다.

1. Http request를 Servlet Container에 보낸다.
2. Servlet Container는 HttpServletRequest, HttpServletResponse 객체를 생성한다.
3. 사용자가 요청한 URL 분석해서 어느 서블릿 요청인지 찾는다.
4. service() 호출 -> doGet() or doPost()
5. HttpServletResponse에 응답을 보냄.

- Spring Container (DI Container)

Spring Container는 Bean의 생명주기를 관리한다.

1. 웹 애플리케이션이 실행되면 Tomcat(WAS)에 의해 web.xml이 loading된다.
2. web.xml에 등록되어 있는 ContextLoaderListener(Java Class)가 생성된다. ContextLoaderListener 클래스는 ServletContextListener 인터페이스를 구현하고 있으며, ApplicationContext를 생성하는 역할을 수행한다.
3. 생성된 ContextLoaderListener는 root-context.xml을 loading한다.
4. root-context.xml에 등록되어 있는 Spring Container가 구동된다. 이 때 개발자가 작성한 비즈니스 로직에 대한 부분과 DAO, VO 객체들이 생성된다.
5. 클라이언트로부터 웹 애플리케이션이 요청이 온다.
6. DispatcherServlet(Servlet)이 생성된다. DispatcherServlet은 FrontController의 역할을 수행한다. 클라이언트로부터 요청 온 메시지를 분석하여 알맞은 PageController에게 전달하고 응답을 받아 요청에 따른 응답을 어떻게 할 지 결정만한다. 실질적은 작업은 PageController에서 이루어지기 때문이다. 이러한 클래스들을 HandlerMapping, ViewResolver 클래스라고 한다.
7. DispatcherServlet은 servlet-context.xml을 loading 한다.
8. 두번째 Spring Container가 구동되며 응답에 맞는 PageController 들이 동작한다. 이 때 첫번째 Spring Container 가 구동되면서 생성된 DAO, VO, ServiceImpl 클래스들과 협업하여 알맞은 작업을 처리하게 된다.