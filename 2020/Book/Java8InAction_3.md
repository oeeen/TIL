# 람다 표현식

메서드로 전달할 수 있는 익명 함수를 단순화한 것. 람다를 이용해서 좀 더 간결하게 구현할 수 있다.

## @FunctionalInterface

말 그대로 함수형 인터페이스임을 나타내는 어노테이션이다. 인터페이스 내에 메서드가 한 개만 존재해야 한다. 그렇지 않을 경우 컴파일 에러가 발생한다. default method의 개수는 상관없다.

## 함수형 인터페이스 사용

### Predicate

`java.util.function.Predicate<T>` 는 test라는 추상 메서드를 정의하고 test는 제네릭 T 형식의 객체를 받아 boolean을 return 한다.

```java
@Test
void predicate() {
    Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
    List<String> strings = new ArrayList<>();
    strings.add("Test");
    strings.add("");
    strings.add("Not Empty");
    strings.add("");
    strings.add("fake");
    List<String> nonEmpty = filter(strings, nonEmptyStringPredicate);

    for (String s : nonEmpty) {
        System.out.println(s);
    }
}

private <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T t : list) {
        if (predicate.test(t)) {
            result.add(t);
        }
    }
    return result;
}
```

### Consumer

`java.util.function.Consumer<T>` 는 제네릭 T 객체를 받아서 void를 반환하는 accept라는 메서드를 정의한다.

```java
@Test
void consumer() {
    forEach(Arrays.asList(1, 2, 3, 4, 5), System.out::println);
}

private <T> void forEach(List<T> list, Consumer<T> c) {
    for (T t : list) {
        c.accept(t);
    }
}
```

### Function

T를 받아서 R 객체를 리턴하는 apply라는 추상 메서드를 정의한다. 하나의 input을 가지고 계산해서 어떤 결과 값을 매핑하는 람다를 정의할 때 이 인터페이스를 활용할 수 있다.

```java
@Test
void function() {
    List<Integer> countOfBlank = map(Arrays.asList("Seongmo Jeon", "One Blank", "Two Blank ", "Thr e e Blank"),
            s -> s.length() - s.replaceAll(" ", "").length());

    for (Integer integer : countOfBlank) {
        System.out.println(integer);
    }
}

private <T, R> List<R> map(List<T> list, Function<T, R> function) {
    List<R> result = new ArrayList<>();
    for (T t : list) {
        result.add(function.apply(t));
    }

    return result;
}
```

## 요약

1. 람다 표현식은 익명함수의 일종이다.
2. 함수형 인터페이스는 하나으 ㅣ추상 메서드만을 정의하는 인터페이스다.
3. 함수형 인터페이스를 기대하는 곳에서만 람다 표현식을 사용할 수 있다.
4. 람다 표현식 전체가 함수형 인터페이스의 인스턴스로 취급된다.
5. Java 8은 IntPredicate 같은 기본형 특화 인터페이스도 제공한다.
6. 람다 표현식의 기대형식을 대상 형식이라고 한다.
7. 메서드 레퍼런스를 이용하면 기존의 메서드 구현을 재사용하고 직접 전달할 수 있다.
