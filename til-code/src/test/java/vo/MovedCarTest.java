package vo;

import dev.smjeon.til.vo.MovedCar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MovedCarTest {
    @Test
    @DisplayName("동등성 확인")
    void equal() {
        MovedCar car1 = new MovedCar("MyCar", 10);
        MovedCar car2 = new MovedCar("MyCar", 10);

        assertEquals(car1, car2);
//        assertSame(car1, car2);
    }
}
