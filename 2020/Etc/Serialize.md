# Serialize

- 자바 직렬화란 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도 사용할 수 있도록 바이트(byte) 형태로 데이터 변환하는 기술과 바이트로 변환된 데이터를 다시 객체로 변환하는 기술(역직렬화)을 아울러서 이야기합니다.
- 시스템적으로 이야기하자면 JVM(Java Virtual Machine 이하 JVM)의 메모리에 상주(힙 또는 스택)되어 있는 객체 데이터를 바이트 형태로 변환하는 기술과 직렬화된 바이트 형태의 데이터를 객체로 변환해서 JVM으로 상주시키는 형태를 같이 이야기합니다.

## 직렬화

자바에서 primitive type과 Serializable의 구현체 객체는 직렬화할 수 있는 기본 조건을 갖는다. 직렬화하는 방법은 java.io.ObjectOutputStream 객체를 이용한다고 써있어서, 그러면 Serializable의 구현체가 아닌 객체는 직렬화가 안되겠지? 라는 생각으로 코드를 짜봤다.

```java
public class Member {
    private String name;
    private String email;
    private int age;

    public Member(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
```

멤버 객체는 위와 같고 이를 직렬화하는 코드는 아래와 같다.

```java
@Test
void serialize() {
    Member member = new Member("Martin", "oeeen3@gmail.com", 21);
    byte[] serializedMember;

    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(member);
            serializedMember = byteArrayOutputStream.toByteArray();
            System.out.println(Base64.getEncoder().encodeToString(serializedMember));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

위 테스트 실행시 java.io.NotSerializableException 이 발생한다. 이 상태에서 Member 객체에 implements Serializable을 추가해주면 직렬화가 가능하게 되면서 정상적으로 byte array가 출력된다.

쉽게 이해하자면 직렬화는 자바의 객체를 바이트로 풀어내는 것이라고 생각하면 될 것 같다.

## 역직렬화

1. 직렬화 대상이 된 객체의 클래스가 클래스 패스에 존재해야 하고 import 되어 있어야 한다.?
    - 직렬화와 역직렬화를 진행하는 시스템이 서로 다를 수 있다는 것을 고려해야한다.
2. 자바 직렬화 대상 객체는 동일한 serialVersionUID를 가지고 있어야 한다.

직렬화가 객체를 풀어내는 것이라면 역직렬화는 이렇게 풀려있는 객체를 다시 객체형태로 만드는 것이라고 생각하면 된다.

## 왜 쓰나

> 자바 시스템 간의 데이터 교환을 위해서 사용한다

자바에서도 csv, json 직렬화 방식을 사용하면 더 쉽지 않을까? -> 정답은 없다. 목적에 따라 적절하게 써야한다.

### 자바 직렬화 장점

- 자바 시스템에서 개발에 최적화 되어있다.
- 직렬화 기본 조건만 지키면 큰 작업 없이 바로 직렬화, 역직렬화 가능하다.

## 언제 쓰나

JVM 메모리에서 상주되어있는 객체 데이터를 그대로 영속화할 때 사용된다. 시스템이 종료되더라도 없어지지 않는 장점, 영속화된 데이터라서 네트워크로 전송도 가능하다. 직렬화된 객체 데이터를 가져와서 역직렬화로 객체로 만들 수 있다.

## serialVersionUID

- serialVersionUID 는 필수 값은 아니다.
- 호환 가능한 클래스는 SUID값이 고정되어 있다.
- SUID가 선언되어 있지 않으면 클래스의 기본 해시값을 사용한다.

### 자바 직렬화를 사용할 때 클래스 구조 변경 시 확인해야 할 것

- serialVersionUID는 개발 시 직접 관리한다.
- serialVersionUID 값이 동일하면 멤버 변수, 메서드 추가는 문제 없다.
- 역직렬화 대상 클래스의 멤버 변수 타입변경은 지양해야 한다.
- 외부에 장기간 저장될 정보는 자바 직렬화 사용을 지양하자.

그냥 자주 변경되는 클래스의 객체는 사용 안하는 것이 좋다.

## 참고자료

- [우아한형제들 기술블로그 - 자바 직렬화, 그것이 알고싶다. 훑어보기편](https://woowabros.github.io/experience/2017/10/17/java-serialize.html)
- [우아한형제들 기술블로그 - 자바 직렬화, 그것이 알고싶다. 실무편](https://woowabros.github.io/experience/2017/10/17/java-serialize2.html)
