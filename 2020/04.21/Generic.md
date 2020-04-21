# Java의 Generic

클래스와 인터페이스 선언에 타입 매개변수가 쓰이면, 제네릭 클래스 또는 제네릭 인터페이스라고 한다. 예를 들어 `List<E>` 가 있다.

이펙티브 자바 3판 - 5장 Generic을 살펴보자

## Raw Type은 사용하지 마라

```java
List stamps = new ArrayList(); // 의도는 Stamp 객체만 넣으려는 것
stamps.add(new Coin()); // 초기 개발자의 의도와는 다르게 stamps에 Coin 객체를 넣었다. 넣을 때는 아무 문제 없다.

Stamp stamp = (Stamp) stamps.get(0); // classCastException 발생
```

이 예외는 컴파일할 때 발견되지 않고, 런타임에 stamps에서 객체를 꺼내어 Stamp객체로 바꾸는데 오류가 발생할 것이다. stamps에 Stamp객체만 계속 넣었다면 아무런 문제가 없겠지만, 어느 순간 Coin 객체를 넣어버린 순간 그 객체를 꺼낼 때 classCastException이 발생하게 될 것이다. 그러면 이 부분을 찾는데 하루종일 걸리게 될 것이다.

그래서 위에서 `Stamp 객체만 넣어라` 라는 주석 대신 컴파일러가 이해할 수 있도록 제네릭을 활용해서 해당 컬렉션에 타입을 지정해줄 수 있는 것이다.

```java
List<Stamp> stamps = new ArrayList<>();
stamps.add(new Coin());
```

이런 식으로 구현하게 되면 컴파일에러를 바로 확인할 수 있다.

로 타입을 쓰면 제네릭이 안겨주는 안전성과 표현력을 모두 잃는다. 그러니까 쓰지말자 ^^ (로 타입을 만들어둔 이유는 이전에 만들어 놓은 코드들에 대한 호환성 때문이다. 새로 짜는 거면 쓰지말자)

### 비한정적 와일드카드 타입(?)

제네릭 타입을 쓰고 싶지만 실제 타입 매개변수가 무엇인지 신경쓰고 싶지 않을 때 ?를 사용하자.

예를 들어보자.

Set<?> 와 로 타입 Set은 뭐가 다를까? - Set<?> 는 안전, Set은 안전하지 않다.(무슨 소리지) ?????

### 핵심 정리 (Raw Type)

Raw Type을 사용하면 런타임에 Exception이 발생할 수 있으니 쓰지말자.
