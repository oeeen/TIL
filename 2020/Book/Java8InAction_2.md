# 동작 파라미터화 코드 전달하기

동작 파라미터화를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다. 동작 파라미터화란 아직 어떻게 실행할 것인지 결정하지 않은 코드 블럭을 의미한다.

동작 파라미터화를 통해서 한 메서드가 다른 동작을 수행하도록 재활용할 수 있다. 유연한 API를 만들기 위해서는 동작 파라미터화라는 도구가 반드시 필요하다.

```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (applePredicate.test(apple)) {
            result.add(apple);
        }
    }

    return result;
}
```

ApplePredicate는 특정 조건에 따라 boolean 값을 반환한다. 예를 들어 초록색인 사과나 150g이상인 사과일 때 이 조건을 구현하는 predicate들을 만들어서 메서드에 파라미터로 넣어주면 된다.

## 익명 클래스

익명 클래스는 블록 내부에 선언된 클래스와 비슷한 개념이다. 말 그대로 이름이 없는 클래스다. 익명 클래스를 사용하면 클래스 선언과 인스턴스화를 동시에 할 수 있다.

```java
List<Apple> greenApples = filterApples(inventory, new ApplePredicate() {
    @Override
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
});
```

처음 방법에서 시도한 방법에 비해 새로운 클래스를 계속해서 만들지 않아도 된다는 점에서 좋을 수 있지만, 여전히 메서드 내부에 불필요하게 코드가 길어진 부분이 있는 것 같아 아쉽다.

## 람다 표현식

위의 코드는 람다 표현식을 이용해 더 간단하게 재구현할 수 있다.

```java
List<Apple> greenApples = filterApples(inventory, apple -> "green".equals(apple.getColor()));
```

## 리스트 형식으로 추상화

```java
public interface Predicate<T> {
    boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T t : list) {
        if (predicate.test(t)) {
            result.add(t);
        }
    }
    return result;
}

List<Apple> heavyApples = filter(inventory, apple -> apple.getWeight() > 150);
```

이런 식으로 만들어서 좀 더 간결하고 확장성 있게 구현할 수 있다.
