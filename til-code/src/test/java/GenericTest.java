import dev.smjeon.til.generic.Coin;
import dev.smjeon.til.generic.MySampleList;
import dev.smjeon.til.generic.Stamp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericTest {

    @Test
    public void classCastExceptionTest() {
        List stamps = new ArrayList(); // 의도는 Stamp 객체만 넣으려는 것
        stamps.add(new Coin()); // 초기 개발자의 의도와는 다르게 stamps에 Coin 객체를 넣었다. 넣을 때는 아무 문제 없다.

        Stamp stamp = (Stamp) stamps.get(0); // classCastException 발생
    }

    @Test
    void classCastExceptionTestUsingGeneric() {
        List<Stamp> stamps = new ArrayList<>();
        stamps.add(new Stamp());
    }

    @Test
    void myList() {
        MySampleList<String> strings = new MySampleList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("Test");
        }
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> strings.get(10));
    }
}
