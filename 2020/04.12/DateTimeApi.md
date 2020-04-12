# Java 8 에 추가된 Date, Time API

java.time 패키지에는 LocalDate, LocalTime, LocalDateTime, Instatn, Duration, Period 등의 새로운 클래스들이 있다.

## LocalDate

LocalDate는 날짜를 표현하는 불변 객체이다.

```java
LocalDate localDate = LocalDate.of(2020, 4, 12);
System.out.println(localDate.getYear()); // 2020
System.out.println(localDate.getMonth()); // APRIL
System.out.println(localDate.getMonthValue()); // 4
System.out.println(localDate.getDayOfMonth()); // 12
System.out.println(localDate.getDayOfWeek()); // SUNDAY
System.out.println(localDate.getDayOfYear()); // 103
LocalDate now = LocalDate.now(); // 2020-04-12
```

Month나 DayOfWeek는 enum이다.

## LocalTime

LocalTime은 시간을 표현하는 불변 객체이다.

```java
LocalTime localTime = LocalTime.of(20, 21, 22);

System.out.println(localTime.getHour()); // 20
System.out.println(localTime.getMinute()); // 21
System.out.println(localTime.getSecond()); // 22
System.out.println(LocalTime.now()); // 현재 시간이 밀리세컨드까지 나온다.
```

## LocalDateTime

LocalDateTime은 LocalDate와 LocalTime을 둘다 같는 클래스다.

```java
/**
* The date part.
*/
private final LocalDate date;
/**
* The time part.
*/
private final LocalTime time;
```

위 처럼 필드로 가지고있다. LocalDateTime의 of 메서드는 다양한 파라미터를 이용해서 사용할 수 있다.

LocalDateTime의 다양한 메서드를 활용해서 LocalDate, LocalTime을 추출할 수도 있고 LocalDate와 atTime이나 LocalTime의 atDate를 활용하여 LocalDateTime으로 만들 수 있다.

```java
LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
System.out.println(localDateTime.toLocalDate()); // 2020-04-12
System.out.println(localDateTime.toLocalTime()); // 20:21:22

System.out.println(localDate.atTime(localTime)); // 2020-04-12T20:21:22
System.out.println(localTime.atDate(localDate)); // 2020-04-12T20:21:22
```

## Duration과 Period

Duration은 두개의 LocalDateTime 이나 두개의 LoclaTime으로 두 시간 사이의 간격을 얻을 수 있다. Duration은 seconds랑 nanoseconds를 필드로 갖는다.

```java
LocalDateTime localDateTime = LocalDateTime.of(2020, 4, 12, 22, 20, 30);
LocalDateTime localDateTime2 = LocalDateTime.of(2020, 4, 12, 23, 23, 32);
Duration duration = Duration.between(localDateTime, localDateTime2);
System.out.println(duration); // PT1H3M2S
```

Duration이 0이면 PT0S라고 출력 된다. Duration의 between 메서드는 Temporal 인터페이스의 구현체를 받는데, LocalDate는 안된다.

Period는 Date사이의 기간을 나타낸다. Period의 between 메서드는 LocalDate만 파라미터로 받는다.

```java
LocalDate localDate = LocalDate.of(2020, 1, 2);
LocalDate localDate2 = LocalDate.of(2020, 1, 3);

Period period = Period.between(localDate, localDate2);

System.out.println(period); // P1D
```

## ZonedDateTime

ZonedDateTime은 ZoneId가 들어있는 LocalDateTime이라고 생각하면 된다.

```java
ZoneId zoneId = ZoneId.of("UTC+9");
ZoneId asia = ZoneId.of("Asia/Seoul");

System.out.println(zoneId); // UTC+09:00
System.out.println(asia); // Asia/Seoul

LocalDate date = LocalDate.of(2020, 4, 12);
ZonedDateTime zonedDateTime = date.atStartOfDay(zoneId);

LocalTime localTime = LocalTime.of(22, 10, 20);
LocalDateTime localDateTime = LocalDateTime.of(date,localTime);

ZonedDateTime asiaTime = localDateTime.atZone(asia);

System.out.println(zonedDateTime); // 2020-04-12T00:00+09:00[UTC+09:00]
System.out.println(asiaTime); // 2020-04-12T22:10:20+09:00[Asia/Seoul]
```
