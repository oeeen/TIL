# Default Method

Default Method는 구현 클래스에서 구현하지 않아도 되는 인터페이스가 포함할 수 있는 메서드다. 메서드 바디는 클래스 구현이 아니라 인터페이스의 일부로 포함된다.

## 왜 Default Method가 나왔을까

외부 공개 라이브러리가 있다고 해보자. 처음에 설계한 인터페이스에서는 필요없던 메서드였다. 그리고 외부에 공개했다.

이 상태에서 이 API에 새로운 메서드를 추가한다. 이 API를 사용하는 모든 코드들에 새로운 메서드의 구현을 추가해야 컴파일 에러 없이 동작 할 것이다.

이를 막기위해 Java8에서는 default method라는 것을 제공한다.

예시로 List Interface 내에도 default method를 살펴보자.

```java
default void replaceAll(UnaryOperator<E> operator) {
    Objects.requireNonNull(operator);
    final ListIterator<E> li = this.listIterator();
    while (li.hasNext()) {
        li.set(operator.apply(li.next()));
    }
}

default void sort(Comparator<? super E> c) {
    Object[] a = this.toArray();
    Arrays.sort(a, (Comparator) c);
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
        i.set((E) e);
    }
}

default Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, Spliterator.ORDERED);
}
```

이 외에도 여러가지 메서드들이 있다. 이 default method들은 이 인터페이스를 구현하는 구현체에서 반드시 구현할 필요는 없다.

List 인터페이스의 최상위 인터페이스인 `Iterable<T>` 를 보면 Iterator() 메서드만 구현하면 되고 나머지는 default method로 선언 된 것을 알 수 있다.

```java
public interface Iterable<T> {
    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    Iterator<T> iterator();

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @implSpec
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @implSpec
     * The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     *
     * @implNote
     * The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @since 1.8
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```
