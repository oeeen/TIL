# BufferedReader와 Scanner

**한 문장으로 정리해서 알고리즘 문제에서 BufferedReader를 Scanner보다 선호하는 이유는 속도 때문이다.**

속도차이 때문이라고 했으니, 실제 속도를 비교해보자.

```java
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
}

```

다음과 같은 테스트를 작성했다. 일단 1억개의 랜덤 숫자를 한 라인에 한개씩 넣은 파일을 생성했다. 그리고 단순히 읽고 걸린 시간을 비교했다.

그 결과.. BufferedReader(6031), Scanner(50115) 로 BufferedReader가 굉장히 빨랐다. 쓰고보니 Scanner는 hasNext의 로직이 들어가므로 이를 없애고 5000만개의 랜덤 숫자만 읽는 것으로 변경해봤다.

```java
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
```

이렇게 작성했을 경우 다음과 같은 결과가 나왔다.

```console
BufferedReader Speed: 2941
Scanner Speed: 23552
```

내부 구현이 어떻게 되어있는지 확인해보지 못했지만, Scanner보다 BufferedReader가 훨씬 빠르다. 알고리즘 문제를 풀 때는 BufferedReader를 활용하도록 하자.
