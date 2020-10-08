# Join

- 한 데이터베이스 내의 여러 테이블의 레코드를 조합하여 하나의 열로 표현한 것이다.
- 따라서 조인은 테이블로서 저장되거나, 그 자체로 이용할 수 있는 ResultSet을 만들어 낸다.

RDB에서는 정규화를 하게 되면 여러 테이블에 관련있는 데이터들이 쪼개지고 각 테이블 끼리 관계를 갖게 된다. 그래서 여러 테이블에 거쳐 존재하는 데이터들을 효과적으로 조회하기 위해서 는 조인이 필요하다.

## Join의 종류

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
