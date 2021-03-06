# WebServer vs. Web Application Server

## WebServer

정적인 컨텐츠를 제공한다. 클라이언트의 요청을 받아 WAS로 전달해주는 역할을 할 수도 있다. WAS와 클라이언트의 요청 사이에서 캐시 역할을 수행할 수도 있다.

## Web Application Server

DB 조회나 다양한 비즈니스 로직 처리를 요구하는 동적인 컨텐츠를 제공하기 위한 어플리케이션 서버다. 트랜잭션 관리나 데이터베이스 접속 커넥션 관리

## 정리

1. 보통 웹서버는 스태틱 파일을 읽어오려는 용도로 사용했었다. 클라이언트의 요청을 WAS로, WAS에서 클라이언트로 응답한다. (nginx, apache)
2. 그런데 시대가 변하면서 단순한 스태틱 페이지 뿐만 아니라 동적으로 어떤 연산이 필요해져서 웹 어플리케이션 서버라는 was가 필요하게 되었다.
3. WAS에서는 DB, 트랜잭션 관리, 비즈니스 로직을 수행한다.
4. 규모가 커질 수록 웹서버와 WAS를 분리한다.
5. fail over, 배포, 무중단 운영, 유지보수의 편의성을 위해 웹서버와 WAS를 분리한다.
