# TCP와 UDP

- TCP: 신뢰성이 중요한 어떤 Application에 의해 사용될 수 있는 신뢰성 있는 연결 지향 프로토콜
- UDP: 오류 제어가 응용층 프로세스에 의해 제공되는 Application에서 단순성과 효율성으로 사용되는 신뢰성 없는 비연결 전송층 프로토콜

## TCP (Transmission Control Protocol)

연결지향, 신뢰성 있는 프로토콜이다.

TCP는 스트림 지향 프로토콜이다. TCP에서는 바이트 스트림으로 데이터를 송, 수신 한다.

## TCP 서비스

- 전이중 통신(full-duplex service): 동시에 양 방향으로 전달 될 수 있다.
- 연결 지향 서비스
    1. 두 사이트 사이에 연결을 설정한다.
    2. 양 방향으로 데이터를 교환한다.
    3. 연결을 종료한다.
- 신뢰성 있는 서비스

### 특징

- Numbering system
  - 세그먼트 헤더에는 Sequence number와 Acknowledgement number 영역이 존재한다.
  - Sequence Number
    - 첫 번째 세그먼트의 Sequence Number는 임의 번호이다.
    - 다른 세그먼트의 Sequence Number는 이전 세그먼트의 Sequence Number + 이전 세그먼트의 바이트 수이다.
  - Acknowledgement Number
    - Ack Number는 상대방으로부터 다음에 받고 싶은 바이트 번호

### 3-way handshaking

1. 클라이언트는 서버에 SYN 세그먼트를 보낸다.
2. 서버는 SYN + ACK 세그먼트를 보낸다. (서버 -> 클라이언트 통신을 위한 SYN과 클라이언트 SYN에 대한 ACK)
3. 클라이언트가 ACK 세그먼트를 보낸다.(ACK Flag와 Acknowledgement 필드 사용)

### SYN flooding attack

Datagram에 있는 source IP를 위조하여 서로 다른 클라이언트로부터 SYN이 들어오는 것처럼 위조해서 SYN을 대량으로 서버에 날릴 수 있다. 서버는 이에 대한 응답으로 SYN + ACK 세그먼트를 날려야하는데, 이 모든 것은 손실된다. 공격자가 많은 서비스 요청을 가진 시스템을 독점한다. denial of service attack으로 알려진 보안 공격 형태에 속한다.

## UDP (User Datagram Protocol)

비연결이고, 신뢰성이 없는 전송 프로토콜이다.

### User Datagram

User Datagram이라고 부르는 UDP 패킷은 각각 2바이트인 4개의 필드로 만들어진 고정된 크기의 8바이트 헤더를 가지고 있다. 1,2 번째 필드는 source, destination의 포트 번호이다. 세 번째 필드는 헤더 + 데이터 = 사용자 데이터그램의 전체 길이이다. 마지막 Checksum이다.

#### 예시

CB84000DOO1COO1C - 16진수 형태의 UDP 헤더라고 한다면, Source Port: (CB84 = 52100), Dest Port(000D = 13), User Datagram 전체 길이: (001C = 28바이트), 데이터 길이: 전체 길이 - 헤더 크기(8바이트) = 20바이트

### UDP 서비스

- 프로세스 대 프로세스 통신
  - Socket Address를 이용
- 비연결 서비스
- 흐름제어 없음
- 오류제어 없음
- 혼잡제어 없음
- UDP는 흐름 및 오류 제어를 하지 않는 간단한 요청-응답 통신을 요구하는 프로세스에 적당하다
- 내부 흐름 및 오류 제어 기법을 가진 프로세스에 적당하다.
- 스트리밍 서비스에 적합하다.
