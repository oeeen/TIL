# StringTokenizer

StringTokenizer는 말 그대로 String을 token으로 쪼갤 수 있게 해준다. StreamTokenizer보다 단순하다. StringTokenizer의 생성자 세개를 살펴보면 다음과 같다.

```java
public StringTokenizer(String str, String delim, boolean returnDelims) {
    currentPosition = 0;
    newPosition = -1;
    delimsChanged = false;
    this.str = str;
    maxPosition = str.length();
    delimiters = delim;
    retDelims = returnDelims;
    setMaxDelimCodePoint();
}

public StringTokenizer(String str, String delim) {
    this(str, delim, false);
}

public StringTokenizer(String str) {
    this(str, " \t\n\r\f", false);
}
```

세 번째 생성자는 tab, newline, carriage return, space, form-feed를 기본 구분자로 사용하고, 그 구분자를 토큰으로 쓰지 않는다.(false)

StringTokenizer는 생성자에서도 알 수 있다시피 구분자(delimeter)를 한개만 쓸 수 있기 때문에, 복잡한 구분자를 사용해야할 때는 Scanner나 split을 사용해야 한다.

이 외에 public method들을 살펴보면, 다음과 같다.

```java
public boolean hasMoreTokens();
public String nextToken();
public String nextToken(String delim);
public boolean hasMoreElements();
public Object nextElement();
public int countTokens();
```

메서드 명만 읽어도 어떤 메서드인지 알 수 있다.

`nextElement()`와 `hasMoreElements`는 Enumeration interface를 implements 하기 때문에 존재한다. (`nextToken()`과 `hasMoreToken()`과 동일한 리턴인데 String대신 Object로 리턴한다.)

테스트 코드로 간단히 어떤 식으로 토큰으로 만드는지 살펴 볼 수 있다.

```java
// tab, newline, carriage return, space, form-feed를 기본 구분자로 사용
@Test
void oneParamConstructor() {
    StringTokenizer st = new StringTokenizer("Hi Hello Hi");

    assertEquals(st.countTokens(), 3);
    assertEquals(st.nextToken(), "Hi");
    assertEquals(st.nextToken(), "Hello");
    assertEquals(st.nextToken(), "Hi");
}

// "," 만 구분자로 사용
@Test
void twoParamConstructor() {
    StringTokenizer st = new StringTokenizer("Hi, Hello, Hi", ",");

    assertEquals(st.countTokens(), 3);
    assertEquals(st.nextToken(), "Hi");
    assertEquals(st.nextToken(), " Hello"); // ,로 구분했기 때문에 그 뒤의 띄어쓰기는 토큰 내에 포함된다.
    assertEquals(st.nextToken(), " Hi");
}

@Test
void threeParamConstructor() {
    StringTokenizer st = new StringTokenizer("Hi, Hello, Hi", ",", true); // 구분자도 토큰에 포함시킨다.

    assertEquals(st.countTokens(), 5); // 구분자도 토큰에 포함되기 때문에 전체 토큰 수는 5개다.
    assertEquals(st.nextToken(), "Hi");
    assertEquals(st.nextToken(), ",");
    assertEquals(st.nextToken(), " Hello"); // ,로 구분했기 때문에 그 뒤의 띄어쓰기는 토큰 내에 포함된다.
    assertEquals(st.nextToken(), ",");
    assertEquals(st.nextToken(), " Hi");

    assertThrows(NoSuchElementException.class, st::nextToken); // 다음 토큰이 없으면 NoSuchElementException이 발생
    assertThrows(NullPointerException.class, () -> new StringTokenizer(null)); // str이 null이면 NullPointerException 발생
}
```

StringTokenizer에서 나오는 Exception은 위의 NoSuchElementException과 NullPointerException 두 개다. 각 Exception은 위 테스트에서 보이는 것처럼 토큰이 없을때, 입력 str이 null일 때 발생한다.
