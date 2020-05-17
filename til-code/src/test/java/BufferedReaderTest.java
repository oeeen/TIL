import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BufferedReaderTest {
    @Test
    void createFile() throws IOException {
        FileWriter fileWriter = new FileWriter("reader_test.txt");
        Random random = new Random();

        for (int i = 0; i < 100_000_000; i++) {
            int num = random.nextInt();
            fileWriter.write(String.valueOf(num));
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    @Test
    void bufferedReaderSpeed() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("reader_test.txt"));
        long start = System.currentTimeMillis();
        while (bufferedReader.readLine() != null) {
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    @Test
    void scannerSpeed() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("reader_test.txt"));
        long start = System.currentTimeMillis();

        while (scanner.hasNext()) {
            scanner.nextLine();
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    @Test
    void fiftyMillionRead() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("reader_test.txt"));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 50_000_000; i++) {
            bufferedReader.readLine();
        }
        long end = System.currentTimeMillis();

        System.out.println("BufferedReader Speed: " + (end - start));

        Scanner scanner = new Scanner(new File("reader_test.txt"));

        start = System.currentTimeMillis();
        for (int i = 0; i < 50_000_000; i++) {
            scanner.nextLine();
        }
        end = System.currentTimeMillis();

        System.out.println("Scanner Speed: " + (end - start));
    }
}
