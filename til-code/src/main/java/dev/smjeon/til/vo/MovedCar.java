package dev.smjeon.til.vo;

import java.util.Objects;

public class MovedCar {
    private final String name;
    private final int currentPosition;

    public MovedCar(final String name, final int currentPosition) {
        this.name = name;
        this.currentPosition = currentPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovedCar movedCar = (MovedCar) o;
        return currentPosition == movedCar.currentPosition &&
                Objects.equals(name, movedCar.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentPosition);
    }
}
