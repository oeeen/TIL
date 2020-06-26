import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexTest {
    @Test
    void emailPattern() {
        Pattern pattern = Pattern.compile("^([a-z.]+)*@([a-z]+)*\\.((com)|(net)|(org))$");

        assertTrue(pattern.matcher("oeeen@gmail.com").matches());
        assertFalse(pattern.matcher("Oeeen3@gmail.com").matches());
        assertFalse(pattern.matcher("aaa@gmail.dev").matches());
    }

    @Test
    void passwordPattern() {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$])[A-Za-z\\d!@#$]{6,20}$");

        assertTrue(pattern.matcher("a1!098").matches());
        assertFalse(pattern.matcher("a1^098").matches());
        assertFalse(pattern.matcher("11!098").matches());
        assertFalse(pattern.matcher("aaaaaa").matches());
        assertFalse(pattern.matcher("aaa111").matches());
        assertFalse(pattern.matcher("a!a11").matches());
    }

    @Test
    void not() {
        Pattern pattern = Pattern.compile("^[^a]martin$");

        assertTrue(pattern.matcher("bmartin").matches());
        assertTrue(pattern.matcher("cmartin").matches());
        assertFalse(pattern.matcher("amartin").matches());
    }

    @Test
    void exactly() {
        Pattern pattern = Pattern.compile("^[ab]{3}$");

        assertTrue(pattern.matcher("aaa").matches());
        assertTrue(pattern.matcher("aab").matches());
        assertTrue(pattern.matcher("aba").matches());
        assertTrue(pattern.matcher("abb").matches());
        assertTrue(pattern.matcher("baa").matches());
        assertTrue(pattern.matcher("bab").matches());
        assertTrue(pattern.matcher("bba").matches());
        assertTrue(pattern.matcher("bbb").matches());
        assertFalse(pattern.matcher("aaaa").matches());
    }
}
