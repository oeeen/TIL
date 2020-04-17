# TCP State

상태 | 설명
--- | ---
CLOSE | 커넥션 없음
LISTEN | Passive open, SYN을 기다리는 상태
SYN-SENT | SYN을 보내고 ACK를 기다리는 상태
SYN-RCVD | SYN+ACK을 보내고 ACK를 기다리는 상태
ESTABLISHED | 커넥션이 생성된 상태, 데이터를 전송할 수 있다.
FIN-WAIT-1 | 첫 FIN이 보내진 상태, ACK를 기다리고 있다.
FIN-WAIT-2 | 첫 FIN에 대한 ACK를 받은 상태, 2번째 FIN을 기다리고 있다.
CLOSE-WAIT | 첫 FIN을 받고 ACK를 보낸 상태, 어플리케이션의 종료를 기다리고 있다.
TIME-WAIT | 2번째 FIN을 받고 ACK를 보낸 상태, 동일 포트와 주소에 커넥션이 생성되지 않도록 하는 시간(2MSL time out)을 기다리는 상태
LAST-ACK | 2번쨰 FIN을 보내고 ACK를 기다리는 상태
CLOSING | 양쪽이 동시에 닫기로 한 상태

## TCP connection close

4-way handshake에서 TCP의 상태에 대해 알아보자

TCP 커넥션 종료는 클라이언트와 서버 어느 쪽에서도 할 수 있지만, 편의상 클라이언트에서 종료 요청했다고 가정한다.

### active close

1. 클라이언트는 FIN을 전송하고 FIN-WAIT-1 상태로 전환
2. 클라이언트는 FIN에 대한 ACK를 수신하고 FIN-WAIT-2 상태로 전환
3. 클라이언트는 FIN을 수신하면 ACK를 전송하고 TIME-WAIT 상태로 전환
4. TIME-WAIT 상태로 2MSL 동안 남아있는다.
5. 타이머가 만료되면 클라이언트는 CLOSED 상태가 된다.

FIN-WAIT-2 상태에서는 일정시간이 지나면 TIME-WAIT로 전환된다.

### passive close

1. 서버는 FIN을 수신하고 ACK를 보낸다. CLOSE-WAIT 상태로 전환
2. 프로세스로부터 passive close 명령을 받으면 서버도 FIN을 전송한다. LAST-ACK 상태로 전환
3. LAST-ACK 상태로 있다가 클라이언트로부터 온 마지막 ACK를 수신하면 CLOSED 상태가 된다.

아래 그림을 보면 connection open 부터 close 까지의 TCP 상태에 대해 알 수 있다.

![connection close](/2020/assets/img/4way-handshake_state.png)

## CLOSE WAIT

FIN-WAIT는 일정 시간이 지나면 TIME-WAIT 상태로 전환 되고, TIME-WAIT는 재 사용이 가능한 상태지만 CLOSE WAIT는 포트를 잡고 있는 프로세스의 종료, 네트워크 재시작 외에는 제거할 방법이 없다.(그래서 어플리케이션의 정상 종료가 필요하다.)

## TIME WAIT

TIME_WAIT 상태가 늘어나면 서버 소켓이 고갈되어 커넥션 타임아웃이 발생할 수 있다고 한다.

하지만 서버는 로컬포트를 사용하지 않는다. 서버가 할당 하는 것은 포트가 아닌 소켓이고 서버의 포트는 최초 bind() 시에 하나만 사용한다. 로컬 포트를 할당하는 것은 클라이언트

그러니까 로컬 포트를 사용하는 것은 클라이언트이므로 서버가 또 다른 서버의 클라이언트로서 접속하여 약 3만개의 동시에 접속하지 않는다면 포트가 고갈되는 일은 발생하지 않을 것이다.

## 참고자료

- [데이터 통신과 네트워크](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788960552890&orderClick=LEa&Kc=) - Chapter 24. 전송층 프로토콜
- [카카오 기술 블로그](https://tech.kakao.com/2016/04/21/closewait-timewait/)
