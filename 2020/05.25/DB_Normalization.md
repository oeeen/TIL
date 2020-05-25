# Database 정규화

> 관계형 데이터베이스 설계에서 중복을 최소화하게 데이터를 구조화하는 프로세스를 정규화라고 한다. (위키백과)

데이터베이스 디자인은 성능상의 이유로 비정규화 될 수도 있다. 그래도 일단 이론적으로 3NF까지는 정규화를 하는 것이 `정규화 되었다.`라고 할 수 있다.

## 좋지 않은 Relational Schema의 문제

1. Redundancy - 동일한 데이터가 반복될 수 있다.
2. Insert Anomaly - 새로운 튜플을 넣을 수 없다.
3. Delete Anomaly - 원하지 않을 때 정보를 잃을 수 있다.
4. Update Anomaly - 여러 곳에서 변경해야 한다.

### 예시 1

PK = {SSN}

SSN | name | age | DNO | dname | budget
--- | --- | --- | --- | --- | ---
11111 | abe | 45 | d1 | network | 500K
22222 | jim | 23 | d2 | security | 700K
33333 | bob | 35 | d2 | security | 700K
44444 | paul | 25 | d2 | security | 700K
55555 | eva | 35 | d3 | database | 900K
66666 | jane | 60 | d3 | database | 900K

위 테이블의 문제점을 보자

1. Redundancy - 동일한 부서 정보가 중복된다.
2. Insert Anomaly - 새로운 부서를 추가한다면, 새로운 부서만을 넣을 수는 없다.(그 부서에 일하는 사람이 없기 때문에, SSN(PK)이 없다)
3. Delete Anomaly - 어떤 부서를 지운다면, 우리는 그 부서에서 일하는 사람들에 대한 정보를 잃을 수 있다.
4. Update Anomaly - 어떤 부서의 이름을 바꾼다면, 해당 부서의 모든 이름을 바꿔야 한다.

> 가이드라인 1 - 다른 Entity 타입을 섞지 마라

위 예시에서는 사람(Employee) Entity와 부서(Department) Entity가 한 테이블 안에 있기 때문에 문제가 된 것이다. 그러므로 이를 두 개의 Entity로 분리해야 한다.

이를 분리하면 다음과 같다.

Employee Table (PK = {SSN}, FK = {DNO})

SSN | name | age | DNO
--- | --- | --- | ---
11111 | abe | 45 | d1
22222 | jim | 23 | d2
33333 | bob | 35 | d2
44444 | paul | 25 | d2
55555 | eva | 35 | d3
66666 | jane | 60 | d3

Department Table (PK = {DNO})

DNO | dname | budget
--- | --- | ---
d1 | network | 500K
d2 | security | 700K
d3 | database | 900K

### 예시 2

SSN | PNO | hours | ename | pname | plocation
--- | --- | --- | --- | --- | ---
11111 | p1 | 45 | abe | notebook | LA
11111 | p2 | 60 | abe | printer | NY
33333 | p1 | 30 | bob | notebook | LA
33333 | p2 | 25 | bob | printer | NY
33333 | p3 | 54 | bob | Tablet | SF
55555 | p1 | 43 | eva | notebook | LA
66666 | p1 | 12 | jane | notebook | LA
66666 | p2 | 50 | jane | printer | NY

> 가이드라인 2 - 중복 없이, Anomaly 없이.

1. 중복과 Anomaly가 없도록 데이터베이스를 디자인 해라.
2. 만약 좋은 ER Schema를 가지고 있다고 해도 중복과 Anomaly가 발생할 수 있다.(Functional dependency 때문에 발생할 수 있다.)
3. 이 문제는 정규화를 통해 해결할 수 있다.

> 가이드라인 3 - Null 없이

가능하면 Null 값을 최소화 하는 것이 좋다.

> 가이드라인 4 - 가짜 튜플 없이

1. 관계에 대한 잘못된 설계는 특정 join 연산에 대해 잘못된 결과를 낼 수 있다.
2. `lossless join` 속성은 join 연산에 대해 의미있는 결과를 보장하는 데 사용된다.
3. `lossless join` 조건을 만족시키도록 관계를 설계 해야한다.
4. 관계를 자연스럽게 결합해서 가짜 튜플을 생성하면 안된다.

## Functional Dependency

- Functional Dependency는 관계 디자인의 좋음을 측정하는 기준으로 사용된다.
- FD는 어트리뷰트의 상호 관계와 의미로부터 파생된 제약조건이다.
- FD는 어트리뷰트의 실제 세계의 제약 조건으로부터 파생된다.
- FD와 key들은 relation의 정규화를 정의하는데 사용된다.

### FD 예시

R = {A, B, C, D, E}
FD: (1) A -> {B, D}
    (2) {B, C} -> E

A | B | C | D | E
--- | --- | --- | --- | ---
a1 | b1 | c1 | d2 | e1
a1 | ? | c2 | ? | e2
a2 | b2 | c4 | d4 | e3
a1 | ? | c3 | ? | e4
a3 | b2 | c4 | d2 | ?

이걸 기반으로 ?에 들어갈 값들을 알아보면, 2번째 줄의 물음표들은 A -> {B, D}에 의해 b1, d2이다. 4번째 줄도 동일하다. 5번째 줄은 {B, C} -> E에 의해 e3로 결정된다.

### 실제 세계 예시

만약 Student 라는 relation이 있다고 생각하고 다음과 같은 튜플들을 갖는다고 해보자.(`STUDENT (ID, club, professor, course, major)`)

만약 학생은 하나의 club에만 가입할 수 있다 라는 제약 조건이 있다면 이는 FD: ID -> club으로 표현 되는 것이다. 그리고 복수전공이 안되고 한 학생은 단 하나의 전공만을 가질 수 있다고 하면, ID -> major 라는 FD가 있는 것이다.

FD는 한 relation 안에서 모든 튜플들이 만족해야 하는 제약 조건이다.

## 1NF (First Normal Form)

어떤 Relation이 다음의 조건을 만족하면 제 1정규화 라고 부른다.

1. 각 튜플들은 primary key에 의해 구분된다.
2. 각 attribute 들은 `atomic` 한 값을 가진다.(즉, multi-value, composite value를 갖지 않는다.)

### Not 1NF 예시

PERSON

SSN | name | phone
--- | --- | ---
11111 | martin | {1234, 3456, 2345}
22222 | seongmo | {2223, 4456}
33333 | smjeon | {3434, 2345}

두 개의 Relation으로 쪼갠다.

SSN | name
--- | ---
11111 | martin
22222 | seongmo
33333 | smjeon

SSN | phone
11111 | 1234
11111 | 3456
11111 | 2345
22222 | 2223
22222 | 4456
33333 | 3434
33333 | 2345

## Normalization 기본 개념

1. 모든 relation은 1NF 라고 가정.
2. anomaly 현상은 bad FD가 존재할 때 발생
3. 이런 bad FD를 단계별로 소거 하는 것이 정규화다
    - key에 부분 종속 되는 FD를 소거: 2NF
    - key에 이행 종속 되는 FD를 소거: 3NF
    - super key가 아닌 것에 종속 되는 FD를 소거: BCNF

## 2NF (Second Normal Form)

### 부분 종속

1. 어떤 key의 멤버인 어트리뷰트를 prime attribute라고 한다.
2. 그 어떤 key의 멤버도 아닌 attribute를 non-prime attribute라고 한다

FD X -> Y가 있으면, Y가 X의 subset에 의존하도록 FD가 있으면, Y는 X에 부분 종속되었다고 한다.

### 2NF

Relation R이 어떤 non-prime attribute도 키 X에 부분 종속 되지 않은 경우 2NF 라고 한다.

그러니까 key와 아무런 관계가 없는 attribute가 키 X에 부분종속 되어 있지 않으면 2NF

### Not 2NF 예시

```default
R = {SSN, PNO, ename, pname, location, hours}
FD: (1) - {SSN, PNO} -> hours
    (2) - SSN -> ename
    (3) - PNO -> {pname, location}
```

이 Relation은 2NF가 아니다. 왜냐하면 non-prime attribute인 ename이 key인 {SSN, PNO}에 부분 종속, non-prime attribute인 {pname, location}이 key에 부분 종속이기 때문

그래서 Relation을 다음과 같이 쪼갠다.

SSN | PNO | hours
--- | --- | ---
11111 | p1 | 45

PK = {SSN, PNO}

SSN | ename
--- | ---
11111 | abe

PK = {SSN}

SSN | pname | location
--- | --- | ---
11111 | notebook | LA

## 3NF (Third Normal Form)

Relation R에 대해 non-prime attribute이 어떤 key에 이행 종속 되어있으면, 3NF이다.

> FD: X -> Y, Y -> Z 일 때, Z는 X에 이행 종속 되어있다고 한다.

또 다른 정의: Relation R의 모든 FD: X -> A에 대해 X는 super key or A는 prime attribute

### Not 3NF 예시

```default
R = {SSN, PNO, ename, pname, location, hours}
FD: (1) - SSN -> DNO
    (2) - DNO -> dname
```

non-prime attribute dname이 key인 SSN에 이행종속이기 때문에 3NF가 아니다.

그래서 Relation을 다음과 같이 쪼갠다.

SSN | ename | age | DNO
--- | --- | --- | ---
11111 | abe | 45 | d1

PK = {SSN}

DNO | dname
--- | ---
d1 | network

PK = {DNO}

## 정규화 연습

```default
R = {ID, course, grade, dept, college}
FD: (1) {ID, grade} -> grade
    (2) ID -> dept
    (3) dept -> college
```

여기서 파악할 수 있는 것은 다음과 같다.

1. key는 ID이다.
2. R은 2NF가 아니다. (dept가 ID에 부분 종속)

그러면 일단 2정규화 한다. Relation을 쪼갠다

ID | course | grade
--- | --- | ---

PK = {ID, course}

ID | dept | college
--- | --- | ---

PK = {ID}

이렇게 쪼개면 2NF지만 다음 테이블이 3NF가 아니다.

```default
R = {ID, dept, college}
FD: (1) ID -> dept
    (2) dept -> college
```

이러면 college(non-prime)가 ID(key)에 이행 종속하기 때문에 3NF가 아니다.

그러면 이 Relation을 또 쪼갠다.

ID | dept
--- | ---

PK = {ID}

dept | college
--- | ---

PK = {dept}

이러면 3NF를 만족한다.

## BCNF (Boyce Codd Normal Form)

Relation R의 모든 FD: X -> A 에 대해 X가 super key이면 BCNF이다.(다른 말로, 모든 FD에 대해 super key제약을 만족한다.)

### Not BCNF 예시

```default
R = {student, course, prof}
FD: (1) {student, course} -> prof
    (2) prof -> course
```

이 relation은 3NF지만, BCNF는 아니다. ({student, course}, {student, prof} 라는 key가 두개 존재) 그래서 {course, prof} 부분이 중복되는 일이 발생한다.

그러면 이 relation은 다음과 같이 쪼개면 된다.

student | course
--- | ---

PK = {student, course}

prof | course
--- | ---

PK = {prof}

모든 BCNF는 FD에 의한 중복과, anomaly가 없다는 것을 보장한다.

## 정리

각 정규화는 이전 것보다 더 엄격하다.

- 모든 2NF는 1NF이다.
- 모든 3NF는 2NF이다.
- 모든 BCNF는 3NF이다.

더 높은 정규화는..

- 더 적은 중복
- 더 적은 Anomaly
- 성능은 떨어질 수 있다.

모든 relation이 BCNF면 좋을 수 있다. 그러나 경우에 따라 트레이드 오프를 생각해야 할 수도 있다.

## 참고자료

- 약 5년 전 학교 다닐 때 강의자료
