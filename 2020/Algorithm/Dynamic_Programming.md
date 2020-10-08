# Dynamic Programming

수학과 컴퓨터 공학, 그리고 경제학에서 동적 계획법(動的計劃法, dynamic programming)이란 복잡한 문제를 간단한 여러 개의 문제로 나누어 푸는 방법을 말한다. 이것은 부분 문제 반복과 최적 부분 구조를 가지고 있는 알고리즘을 일반적인 방법에 비해 더욱 적은 시간 내에 풀 때 사용한다. (위키백과)

## 설명

동적 계획법의 원리는 매우 간단하다. 일반적으로 주어진 문제를 풀기 위해서, 문제를 여러 개의 하위 문제(subproblem)로 나누어 푼 다음, 그것을 결합하여 최종적인 목적에 도달하는 것이다. 각 하위 문제의 해결을 계산한 뒤, 그 해결책을 저장하여 후에 같은 하위 문제가 나왔을 경우 그것을 간단하게 해결할 수 있다. 이러한 방법으로 동적 계획법은 계산 횟수를 줄일 수 있다. 특히 이 방법은 하위 문제의 수가 기하급수적으로 증가할 때 유용하다.

## DP 조건

- 작은 문제가 반복이 발생할 경우
- 반복된 문제의 답은 항상 같다.
- 모든 작은 문제는 한번만 계산해야 한다.

## 예시

```script
var m := map(0 → 1, 1 → 1)

function fib(n)
 if n not in keys(m)
  m[n] := fib(n-1) + fib(n-2)
 return m[n]
```

자바로 짜보면

```java
import java.util.HashMap;
import java.util.Map;

public class Fibonacci {
    private Map<Integer, Integer> mem = new HashMap<>();

    private Fibonacci() {
        mem.put(0, 1);
        mem.put(1, 1);
    }

    public static Fibonacci getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final Fibonacci INSTANCE = new Fibonacci();
    }

    public int calc(int n) {
        if (!mem.containsKey(n)) {
            mem.put(n, calc(n - 1) + calc(n - 2));
        }
        return mem.get(n);
    }
}
```

이런 식으로 구현했다. 일단 객체 만들어질 때마다 계산을 새로하고 싶지 않아서 싱글턴으로 했다.

```java
Fibonacci instance = Fibonacci.getInstance();
System.out.println(instance.calc(5));
```

여기서 fibonacci(5) 구하는 것을 디버깅 돌려본다. 그러면 아래의 순서대로 구해지는 것을 알 수 있다.

### fib(5)를 구하는 방법

1. fib(4) + fib(3)
2. (fib(3) + fib(2)) + (fib(2) + fib(1))
3. ((fib(2) + fib(1)) + (fib(1) + fib(0))) + ((fib(1) + fib(0)) + fib(1))
4. (((fib(1) + fib(0)) + fib(1)) + (fib(1) + fib(0))) + ((fib(1) + fib(0)) + fib(1))

![fib 0](/2020/assets/img/fib0.png)

결국 최종 결과는 fibonacci 5까지 맵에 저장된 상태이다.

![fib 5](/2020/assets/img/fib5.png)

그 이후에는 이보다 작은 문제는 새로 계산할 필요없이 바로 구할 수 있다. 이게 동적계획법의 핵심인 듯 싶다.

## 참고자료

- [위키백과 - 동적 계획법](https://ko.wikipedia.org/wiki/%EB%8F%99%EC%A0%81_%EA%B3%84%ED%9A%8D%EB%B2%95)
