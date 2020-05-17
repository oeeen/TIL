import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenizerTest {
    @Test
    void oneParamConstructor() {
        StringTokenizer st = new StringTokenizer("Hi Hello Hi");

        assertEquals(st.countTokens(), 3);
        assertEquals(st.nextToken(), "Hi");
        assertEquals(st.nextToken(), "Hello");
        assertEquals(st.nextToken(), "Hi");
    }

    @Test
    void twoParamConstructor() {
        StringTokenizer st = new StringTokenizer("Hi, Hello, Hi", ",");

        assertEquals(st.countTokens(), 3);
        assertEquals(st.nextToken(), "Hi");
        assertEquals(st.nextToken(), " Hello");
        assertEquals(st.nextToken(), " Hi");
    }

    @Test
    void threeParamConstructor() {
        StringTokenizer st = new StringTokenizer("Hi, Hello, Hi", ",", true);

        assertEquals(st.countTokens(), 5);
        assertEquals(st.nextToken(), "Hi");
        assertEquals(st.nextToken(), ",");
        assertEquals(st.nextToken(), " Hello");
        assertEquals(st.nextToken(), ",");
        assertEquals(st.nextToken(), " Hi");

        assertThrows(NoSuchElementException.class, st::nextToken);
        assertThrows(NullPointerException.class, () -> new StringTokenizer(null));
    }
}
