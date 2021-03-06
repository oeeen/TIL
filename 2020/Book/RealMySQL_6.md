# Real MySQL 6장 - 실행 계획

DBMS의 쿼리 실행 또한 같은 결과를 만들어 내는 데 한 가지 방법만 있는 것은 아니다.

## 쿼리 실행 절차

1. 사용자로부터 요청된 SQL을 잘게 쪼개어 MySQL 서버가 이해할 수 있는 수준으로 분리한다.
2. SQL의 파싱 정보(parse tree)를 확인하며 어떤 테이블에서 읽고 어떤 인덱스를 이용해서 테이블을 읽을지 선택한다.
3. 2에서 결정된 테이블의 읽기 순서나 선택된 인덱스를 이용해 스토리지 엔진으로부터 데이터를 가져온다.

MySQL 서버는 1단계에서 SQL parse tree를 이용해서 쿼리를 실행한다.

2단계에서는 불필요한 조건 제거, 복잡한 연산 단순화, 조인이 있을 경우 어떤 순서로 테이블을 읽을 지 결정, 각 테이블에 사용된 조건과 인덱스 통계 정보를 이용해 사용할 인덱스 결정, 가져온 레코드들을 임시 테이블에 넣고 다시 한번 가공해야 하는지 결정.. 등의 작업을 처리한다. 이 단계를 거쳐 쿼리의 `실행계획`이 만들어진다.(MySQL 서버의 옵티마이저에서 처리한다)

3단계에서는 수립된 실행계획대로 스토리지 엔진에 레코드를 읽어오도록 요청하고, 받은 레코드를 조인하거나 정렬하는 작업은 MySQL 엔진에서 수행한다.

## 옵티마이저의 종류

1. 비용 기반 최적화
    - 대상 테이블의 레코드 건수나 선택도 등을 고려하지 않고, 옵티마이저에 내장된 우선 순위에 따라 실행 계획을 수립한다.
2. 규칙 기반 최적화
    - 쿼리를 처리하기 위한 여러 가지 가능한 방법을 만들고, 각 단위 작업의 비용 정보와 대상 테이블의 예측된 통계 정보를 이용해 각 실행 계획별 비용을 산출하고, 최소 비용의 실행계획을 선택한다.

## 실행 계획 분석

MySQL에서 쿼리의 실행 계획을 확인하려면 EXPLAIN 명령을 사용하면 된다. EXPLAIN EXTENDED나 EXPLAIN PARTITIONS 명령으로 더 상세한 실행 계획을 확인할 수도 있다.

사용 방법은 단순하게 실행 계획을 확인하고 싶은 쿼리 앞에 EXPLAIN 명령을 붙이면 된다.
