# Database 관련

## Join

- 한 데이터베이스 내의 여러 테이블의 레코드를 조합하여 하나의 열로 표현한 것이다.
- 따라서 조인은 테이블로서 저장되거나, 그 자체로 이용할 수 있는 ResultSet을 만들어 낸다.

RDB에서는 정규화를 하게 되면 여러 테이블에 관련있는 데이터들이 쪼개지고 각 테이블 끼리 관계를 갖게 된다. 그래서 여러 테이블에 거쳐 존재하는 데이터들을 효과적으로 조회하기 위해서 는 조인이 필요하다.

### Inner Join

```sql
SELECT *
FROM employee INNER JOIN department
ON employee.DepartmentID = department.DepartmentID;
```

이런 식으로 employee와 department 테이블 내에서 같은 DepartmentId를 가진 결과가 나오게 된다.

### Outer Join

Left Outer Join, Right Outer Join, Full Outer Join을 생각해볼 수 있다.

일단 Left Outer Join은 우측 테이블에 조인할 컬럼의 값이 없는 경우 사용한다. 좌측 테이블의 모든 데이터를 포함하는 결과 집합을 생성한다.

ON 조건에 맞지 않는 데이터들은 오른쪽 테이블의 값이 NULL인 것으로 채워져서 나오게 될 것이다.

Right Outer Join은 Left와 반대다. Right Table에 있는 모든 데이터를 포함하는 결과 집합을 생성한다.

예시를 보면 알기 쉽다.

#### Employee Table

Name | DepartmentId
--- | ---
Martin | 21
Seongmo | 22
Seonghwan | 23
Pobi | 24
Woni | 24
Jun | 24
Brown | Null

#### Department Table

Name | DepartmentId
--- | ---
영업부 | 21
마케팅 | 22
기획실 | 23
개발실 | 24
신사업부 | 25

이런 식으로 있다고 했을 때 DepartmentId로 Left Outer Join 했을 경우 아래와 같은 결과가 나온다.

```sql
SELECT *
FROM employee LEFT OUTER JOIN department
ON employee.DepartmentID = department.DepartmentID;
```

employee.Name | employee.DepartmentId | department.Name | department.DepartmentId
--- | --- | --- |---
Martin | 21 | 영업부 | 21
Seongmo | 22 | 마케팅 | 22
Seonghwan | 23 | 기획실 | 23
Pobi | 24 | 개발실 | 24
Woni | 24 | 개발실 | 24
Jun | 24 | 개발실 | 24
Brown | Null | Null | Null

Brown은 DepartmentId가 null이지만 왼쪽 테이블(employee)에 속해있는 데이터이기 때문에 결과 집합에 들어가게된다.

Right Outer Join은 Left와 반대이며, Full Outer Join은 양쪽 테이블에 속한 모든 데이터들에 대한 결과 집합을 만든다.

## Database에서 Index

인덱스는 데이터베이스에서 테이블에 대한 동작 속도를 높여주는 자료구조이다. 테이블 내의 1개 이상의 컬럼을 이용해서 생성할 수 있다. 특정 데이터를 검색하기 위해서 테이블의 레코드를 풀스캔하는 것이 아니라, 인덱스가 적용된 컬럼의 테이블을 따로 파일로 저장해놓고 그 파일을 검색한다.

Clustered Index - 인덱스를 매칭시키기 위해서 데이터 블럭을 분명한 순서로 바꾼다. Clustered index는 read 속도를 향상 시킨다.

Non-Clustered Index - 데이터는 임의의 순서, 논리적 순서는 인덱스에 의해 지정됨. row의 물리적 순서는 인덱스 순서와 동일하지 않다.

DBMS 의 인덱스는 항상 정렬된 상태를 유지하기 때문에 원하는 값을 탐색하는데는 빠르지만 새로운 값을 추가하거나 삭제, 수정하는 경우에는 쿼리문 실행 속도가 느려진다. 사용하지 않는 인덱스는 제거하자. 카디널리티가 높은 칼럼에 인덱스를 생성해주는 것이 좋다. 흔히 사용하는 인덱스는 B-tree 인덱스다.

인덱스가 생성되면 테이블과 매핑된 또다른 테이블이 생성된다고 생각하면 좋다. 인덱스 컬럼을 기준으로 sorting되어 저장된다. 특정 조건에 대해 검색을 한다고 하면 시작점을 지정해서 거기서부터 스캔(Index Range Scan)을 할 수 있다고 생각하면 됨. 인덱스에서 먼저 데이터를 찾고 그 테이블로 매핑된 곳을 가서 나머지 데이터들을 꺼내오는 방식. 인덱스가 해당 테이블 블럭의 주소를 가지고 있다고 생각하면 된다.

Where절에 자주 등장하는 컬럼, Order By절에 자주 등장하는 컬럼을 인덱스로 구성하면 좋다. 그렇다고 마구잡이로 인덱스를 생성하면 안된다. 테이블이 가지고 있는 전체 데이터 양의 10에서 15프로 일때 효율적이고 그 이상일 떈 풀스캔이 더 빠르다.(?)
