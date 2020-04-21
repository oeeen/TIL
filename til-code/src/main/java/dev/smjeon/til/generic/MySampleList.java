package dev.smjeon.til.generic;

import java.util.Arrays;

public class MySampleList<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private static final Object[] DEFAULT_CAPACITY_ELEMENT = {};

    private Object[] elements;
    private int size;

    public MySampleList() {
        this.elements = DEFAULT_CAPACITY_ELEMENT;
    }

    public boolean add(E e) {
        checkEmpty();
        elements[size++] = e;
        return true;
    }

    private void checkEmpty() {
        if (elements == DEFAULT_CAPACITY_ELEMENT) {
            elements = Arrays.copyOf(elements, DEFAULT_CAPACITY);
        }
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }
}
