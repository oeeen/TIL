package dev.smjeon.til.java8;

@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
