# 정규표현식

> 정규 표현식(正規表現式, 영어: regular expression, 간단히 regexp 또는 regex, rational expression) 또는 정규식(正規式)은 특정한 규칙을 가진 문자열의 집합을 표현하는 데 사용하는 형식 언어이다.

알고리즘 문제를 풀다보면 정규식으로 어떤 패턴을 맞춰야 하는 문제가 가끔 나온다. 그런 의미에서 정규표현식에 대해 알아보자.

## 양의 지정

기호 | 뜻
--- | ---
? | 0번 또는 1번
* | 0번 이상
+ | 1번 이상
{n} | 정확히 n번
{min, } | 최소 min번 이상
{min, max} | 최소 min번 이상, max 이하 (min, max 경계도 포함)

## 문법

식 | 기능 | 설명
--- | --- | ---
. | 문자 | 문자 1개
[] | 문자 클래스 | []내부의 문자 중 한개를 선택한다. - 와 함께 쓰면 범위를 지정할 수 있다. 예를 들어 `[a-z]`는 알파벳 소문자 전체이다.
[^ ] | 부정 | 내부의 문자를 제외한 나머지를 선택한다. 예를 들어 `[^a]martin`은 amartin만을 제외하고 모든 문자가 허용된다.
^ | 처음 | 문자열이나 행의 처음
$ | 끝 | 문자열이나 행의 끝
() | 하위식 | 여러 식을 하나로 묶을 수 있다. 흔히 사용하는 괄호의 용도라고 보면 된다. 예를 들어 `abcd|adcd`라는 표현식은 `a(b|d)cd` 라고 표현할 수 있다.
\n | 일치하는 n번째 패턴 | 일치하는 패턴들 중 n번째를 선택하며, 여기에서 n은 1에서 9 중 하나가 올 수 있다.
* | 0번 이상 | 해당하는 문자가 0번 이상 나타난다. 예를 들어 `a*b`는 b, ab, aab, aaab와 일치한다.
+ | 1번 이상 | 해당하는 문자가 1번 이상 나타난다. 예를 들어 `a+b`는 ab, aab, aaab와 일치한다. (b는 포함하지 않는다.)
{n} | n번 | 해당하는 문자가 정확히 n번 나타난다. 예를 들어 `[ab]{3}`은 aaa, aab, aba, abb, baa, bab, bba, bbb와 일치한다.
{n, m} | n번 이상 m번 이하 | 해당하는 문자가 n번 이상 m번 이하 나타난다. 예를 들어 비밀번호 글자수 제한에서 사용한다.(8자이상 20자 미만이면 {8,20})
? | 0번 또는 1번 | 해당하는 문자가 0 또는 1번 나타난다. 예를 들어 `ab?`라면 a, ab만 일치한다.
| | 선택 | 흔히 사용하는 or라고 생각하면 된다.

## 문자클래스

POSIX | Vim | ASCII | 설명
[:alnum:] |  | [A-Za-z0-9] | 영숫자
[:alpha:] | \a | [A-Za-z] | 알파벳 문자
[:blank:] | \s | [ \t] | 공백과 탭
[:cntrl:] |  | [\x00-\x1F\x7F] | 제어 문자
[:digit:] | \d | [0-9] | 숫자
[:graph:] |  | [\x21-\x7E] | 보이는 문자
[:lower:] | \l | [a-z] | 소문자
[:print:] | \p | [\x20-\x7E] | 보이는 문자 및 공백 문자
[:upper:] | \u | [A-Z] | 대문자
[:xdigit:] | \x | [A-Fa-f0-9] | 16진수

## 전방탐색과 후방탐색

### 전방탐색

전방 탐색은 작성한 패턴에 일치하는 것이 존재 해도 그 값이 제외되어서 나오는 패턴이다. 기호는 ?=이다.

예를 들어 `^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$])[A-Za-z\d!@#$]{6,20}$`이런 패턴이 존재할 수 있다. 알파벳 대소문자, 숫자, 특수문자 !@#$에 일치하는 것이 각각 존재 해야 `[A-Za-z\d!@#$]{6,20}` 이 부분의 패턴을 확인할 수 있다. 그러므로 알파벳 대소문자, 숫자, 특수문자를 반드시 하나 이상 포함하는 6자이상 20자 미만의 패스워드 패턴으로 사용할 수 있다.

### 후방탐색

후방 탐색은 전방탐색과는 반대로 뒤에 있는 문자열을 탐색하는 것이다. 기호는 ?<=이다.

예를 들어 영어 대소문자로 된 문자열에서 최초 대문자 이후에 나타나는 문자가 소문자인지 확인하려는 패턴은 `(?<=.[A-Z])[a-z]+` 처럼 사용하면 될 것이다.

## 예시

```java
@Test
void emailPattern() {
    Pattern pattern = Pattern.compile("^([a-z.]+)*@([a-z]+)*\\.((com)|(net)|(org))$");
    // 영 소문자의 아이디 + @ + 영 소문자 도메인 + . + com 또는 net 또는 org 의 문자열

    assertTrue(pattern.matcher("oeeen@gmail.com").matches());
    assertFalse(pattern.matcher("Oeeen3@gmail.com").matches());
    assertFalse(pattern.matcher("aaa@gmail.dev").matches());
}
@Test
void passwordPattern() {
    Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$])[A-Za-z\\d!@#$]{6,20}$");
    // 영어 대소문자, 숫자, 특수문자(!@#$)를 반드시 하나 이상 포함하고 6자 이상 20자 이하의 문자열

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
    // amartin 을 제외한 bmartin, cmartin 같은 문자열

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
```

## 참고자료

- [위키백과 - 정규표현식](https://ko.wikipedia.org/wiki/%EC%A0%95%EA%B7%9C_%ED%91%9C%ED%98%84%EC%8B%9D)
