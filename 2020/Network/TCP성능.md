# TCP의 성능 관련 요소

- TCP handshake
- TCP slow start
- nagle algorithm
- TIME_WAIT의 누적과 포트 고갈

## TCP handshake

새로운 TCP 커넥션을 만들기 위해서 3-way handshake를 한다.

여기서 초기 SYN - SYN + ACK 로 이어지는 부분에서 지연이 발생할 수 있다.

## TCP slow start

TCP는 커넥션이 만들어진 후 시간에 따라 커넥션의 최대 속도가 올라간다. 처음에는 한 번에 전송할 수 있는 패킷의 수를 제한하고 데이터가 점차 성공적으로 전송됨에 따라 이 제한을 늘려나간다.

## nagle algorithm

TCP 세그먼트는 다른 플래그와 헤더들을 포함하고 있기 때문에 아주 작은 데이터를 전송하는데는 비효율적일 수 있다. 그렇기 때문에 nagle algorithm에서는 TCP 세그먼트가 최대 크기가 되지 않으면 전송하지 않는다.

그래서 데이터 세그먼트를 다 채우지 못하면 데이터가 계속해서 지연되고 전송되지 못하는 경우가 있다.

그래서 HTTP 스택에 TCP_NODELAY 라는 파라미터 값을 설정하여 nagle algorithm을 비활성화 할 수도 있다.

## TIME_WAIT의 누적과 포트고갈

TCP 커넥션을 끊으면 커넥션의 IP주소와 포트 번호를 메모리의 작은 control block에 기록해둔다.(일정 시간동안은 같은 주소와 포트번호를 가진 커넥션이 생성되지 않도록)

커넥션이 종료된 후 세그먼트의 최대 생명주기의 약 두 배 정도의 시간(여기서는 2분이라고 가정)동안은 동일한 주소, 포트번호를 가진 커넥션이 생성되지 않는다.

클라이언트가 서버에 접속할 때 새로운 커넥션을 생성하기 위해 항상 새로운 발신지 포트를 사용한다. 사용할 수 있는 발신지 포트를 약 60,000개로 가정했을 때, 최대 2분동안은 동일한 포트, 주소로 커넥션을 생성할 수 없으므로 초당 최대 500개로 커넥션이 제한 된다. 그래서 초당 최대 500개 이상의 커넥션이 생성되어야 한다면 TIME_WAIT 포트 고갈이 일어날 것이다.

## 참고자료

- HTTP 완벽 가이드 - 4장. 커넥션 관리
