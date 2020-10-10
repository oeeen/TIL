package dev.smjeon.til.java8;

import java.util.ArrayList;
import java.util.List;

public class BehaviorParameterization {
    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(200, "green"));
        inventory.add(new Apple(110, "green"));
        inventory.add(new Apple(120, "green"));
        inventory.add(new Apple(130, "red"));
        inventory.add(new Apple(160, "red"));
        inventory.add(new Apple(230, "red"));
        List<Apple> greenApples = filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "green".equals(apple.getColor());
            }
        });

//        greenApples = filterApples(inventory, apple -> "green".equals(apple.getColor()));
        List<Apple> heavyApples = filter(inventory, apple -> apple.getWeight() > 150);
    }

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (color.equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (applePredicate.test(apple)) {
                result.add(apple);
            }
        }

        return result;
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
}
