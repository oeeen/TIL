import dev.smjeon.til.java8.Predicate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalInterfaceTest {
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

    @Test
    void consumer() {
        forEach(Arrays.asList(1, 2, 3, 4, 5), System.out::println);
    }

    private <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t : list) {
            c.accept(t);
        }
    }

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
}
