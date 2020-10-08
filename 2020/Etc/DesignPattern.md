# Design Pattern

## 전략패턴

객체들이 할 수 있는 행위 각각에 대해 전략 클래스를 생성하고, 유사한 행위들을 캡슐화 하는 인터페이스를 정의하여, 객체의 행위를 동적으로 바꾸고 싶은 경우 직접 행위를 수정하지 않고 전략을 바꿔주기만 함으로써 행위를 유연하게 확장하는 방법을 말합니다.

간단히 말해서 객체가 할 수 있는 행위들 각각을 전략으로 만들어 놓고, 동적으로 행위의 수정이 필요한 경우 전략을 바꾸는 것만으로 행위의 수정이 가능하도록 만든 패턴입니다.

## 프록시 패턴

- 어떤 객체에 대한 접근을 제어하기 위한 용도로 대리인이나 대변인에 해당하는 객체를 제공하는 패턴
- 가상 프록시
  - 생성하는데 많은 비용이 드는 객체를 대신하는 역할
  - 실제로 진짜 객체가 필요하게 되기 전까지 객체의 생성을 미루게 해준다.
  - Head First Design Pattern에 나온 예시를 들어보면
    - Image Icon을 로드하는데 네트워크를 통해 오랜 시간이 걸릴 수 있기 때문에 ImageIcon의 ImageProxy를 만들 수 있다.
    - 이 ImageProxy에서는 실제 ImageIcon에서 모든 이미지가 로드 될 때까지 다른 작업을 할 수 있다.(로딩중입니다 라는 메세지를 띄운다거나)
    - ImageIcon이 이미지를 로딩이 완료 되면, ImageIcon이 이미지를 화면에 띄우게 된다.

JPA에서 Lazy Loading도 여기서 나오는 가상 프록시와 같은 목적으로 사용한다고 할 수 있다. 실제 객체를 생성하는 데(데이터베이스로부터 조회할 데이터가 많다.)는 많은 비용이 들기 때문에, 실제 안에 내용은 없는 프록시 객체를 생성한 후, 그 객체를 실제로 사용할 때 내용을 데이터베이스로부터 불러오는 것이기 때문이다.

말로만 풀어쓰니 이해가 잘 안될 수 있는데, 실제 책을 보면 이해가 잘 된다.

- 프록시 vs. 데코레이터
  - 프록시는 객체에 대한 접근을 제어한다.
  - 클라이언트의 입장에서는 자신이 요청을 하는 것이 프록시인지 실제 객체인지 알 수 없다.
  - 프록시가 존재할 때 실제 객체는 존재하지 않는 상태일 수 있다.
  - 데코레이터는 객체에 행동을 추가해준다.

## 어댑터 패턴

어댑터는 220V -> 110V 로 전환해주는 어댑터라고 생각하면 된다. 어떤 인터페이스를 클라이언트에서 요구하는 형태의 인터페이스에 적용시켜 준다.

정의: 한 클래스의 인터페이스를 클라이언트에서 사용하고자 하는 다른 인터페이스로 변환합니다. 어댑터를 이용하면 인터페이스 호환성 문제 때문에 같이 쓸 수 없는 클래스들을 연결해서 쓸 수 있습니다.

디자인 패턴 책에 나와있는 예제를 써보면,

```java
public interface Duck {
    public void quack();
}
public class WildDuck implements Duck {
    @Override
    public void quack() {
        System.out.println("Quack");
    }
}

public interface Turkey {
    public void gobble();
}

public class WildTurkey implements Turkey {
    @Override
    public void gobble() {
        System.out.println("Gobble gobble")
    }
}
```

이런 상황에서 Duck 객체를 이용해서 Turkey 객체를 만들어야 한다고 해보자. 이럴 경우 다음과 같이 하면 된다.

```java
public class TurkeyAdapter implements Duck {
    private Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }
}
```

이런 식으로 요청을 위임하는 식으로 어댑터를 구현할 수 있다.

내 생각이지만, Spring에서 HandlerAdapter의 구현체 중 SimpleControllerHandlerAdapter, HttpRequestHandlerAdapter를 보면 Handler를 사용하고자 하는 Controller, HttpRequest의 형태로 변환하여 메서드를 호출한다.

```java
public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    @Nullable
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return ((Controller) handler).handleRequest(request, response);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        if (handler instanceof LastModified) {
            return ((LastModified) handler).getLastModified(request);
        }
        return -1L;
    }
}
public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HttpRequestHandler);
    }

    @Override
    @Nullable
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        ((HttpRequestHandler) handler).handleRequest(request, response);
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        if (handler instanceof LastModified) {
            return ((LastModified) handler).getLastModified(request);
        }
        return -1L;
    }
}
```

HandlerAdapter를 통해 Handler 인터페이스를 원하는 인터페이스(Controller, HttpRequest)로 변경한다.

## 템플릿 메서드 패턴

> 실행 과정/단계는 동일한데 각 단계 중 일부의 구현이 다른 경우에 사용할 수 있는 패턴이 템플릿 메서드 패턴이다.

또는

> 상속을 통해 슈퍼클래스의 기능을 확장할 때 사용하는 가장 대표적인 방법. 변하지 않는 기능은 슈퍼클래스에 만들어두고 자주 변경되며 확장할 기능은 서브클래스에서 만들도록 한다.

1. 실행 과정을 구현한 상위클래스
2. 실행 과정의 일부 단계를 구현한 하위 클래스

로 구성된다.

템플릿 메서드 패턴의 특징은 하위 클래스가 아닌 상위 클래스에서 흐름 제어를 한다는 것이다. 일반적인 경우 하위 타입이 상위 타입의 기능을 재사용할지 여부를 결정하기 때문에, 흐름 제어를 하위타입이 하게 된다.

예를 들어보면 한번에 이해할 수 있다.

```java
// AbstractClass.java
public abstract AbstractClass {

    protected abstract void method1();

    protected abstract void method2();

    public void templateMethod() {
        method1();
        method2();
    }
}


// ConcreteClass.java
public ConcreteClass extends AbstractClass {
    @Override
    protected void method1() {
        // Do Something
    }

    @Override
    protected void method2() {
        // Do Something
    }
}
```

이렇게 작성하고 실제로 ConcreteClass를 사용하는 곳에서 templateMethod를 사용하여 ConcreteClass에서 구현한 method1, method2를 사용한다.

위에서 말했듯 상위 클래스에서 흐름을 제어하고, 그 흐름에 따라 하위 클래스에서 구현한 메서드를 실행하는 것을 볼 수 있다.
