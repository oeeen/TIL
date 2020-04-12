# Http Method

## GET

- Request Body X
- Response Body O
- 서버의 상태를 변경 시키지 않음(Read-only)
- 같은 요청은 언제나 같은 결과를 낸다. (idempotent)
- Response Cacheable
- HTML Form에서 허용

## HEAD

- GET과 같지만 Response Body가 없다.
- 실제 문서를 요청하는 것이 아니라 문서 정보를 요청

## POST

- 데이터를 서버로 보낸다.
- Request body는 content-type에 따라 달라진다.
- POST는 수행할 때마다 계속 수행된다.(POST article 하면 계속 글 써진다.)

## PUT

- 멱등성을 가진다. (여러번 호출해도 항상 같은 결과)
- 데이터를 서버로 보낸다.
- 새로운 데이터를 만들거나 업데이트한다.

## DELETE

- 특정 리소스를 삭제한다.
- 정상 Response로 202(Accepted), 204(No Content), 200(OK)

## CONNECT

- 목적 리소스로 식별되는 서버로의 터널을 맺습니다.?
- 프록시 기능 요청시 사용합니다.

## OPTIONS

- 타겟 리소스의 커뮤니케이션 옵션을 나타낸다.(지원되는 메소드 종류를 확인합니다.)

## TRACE

- 목적 리소스의 경로를 따라 메시지 loop-back 테스트를 한다.
- 요청 리소스가 수신되는 경로를 보여줌

## PATCH

- 리소스의 부분만을 수정하는 데 쓰입니다.?

| method        | GET  | HEAD | POST | PUT  | DELETE |
| ------------- | ---- | ---- | ---- | ---- | ------ |
| Request Body  | X    | X    | O    | O    | ?      |
| Response Body | O    | X    | O    | X    | ?      |
| Safe          | O    | O    | X    | X    | X      |
| Idempotent    | O    | O    | X    | O    | O      |
| Cacheable     | O    | O    | O    | X    | X      |
| HTML Form     | O    | X    | O    | X    | X      |
